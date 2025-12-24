package com.example.cblverify;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.couchbase.lite.Blob;
import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@RunWith(AndroidJUnit4.class)
public class BasicCblInstrumentedTest {

    private static final String DB_NAME = "release-verification";
    private Database db;

    @Before
    public void setUp() throws Exception {
        Context ctx = ApplicationProvider.getApplicationContext();
        CouchbaseLite.init(ctx);

        safeDeleteDb();

        DatabaseConfiguration config = new DatabaseConfiguration();
        db = new Database(DB_NAME, config);

        assertNotNull(db.getPath());
    }

    @After
    public void tearDown() {
        try {
            if (db != null) db.close();
        } catch (Exception ignore) {
        } finally {
            db = null;
        }

        safeDeleteDb();
    }

    private void safeDeleteDb() {
        try {
            if (Database.exists(DB_NAME, null)) {
                Database.delete(DB_NAME, null);
            }
        } catch (CouchbaseLiteException ignore) {
        }
    }

    @Test
    public void basicDatabaseAndBlobTest() throws Exception {
        Log.i("CBL-VERIFY", "Running flavor=" + BuildConfig.CBL_FLAVOR);
        assertEquals("core", BuildConfig.CBL_FLAVOR);
        Collection coll = db.getDefaultCollection();
        assertNotNull(coll);

        MutableDocument doc = new MutableDocument();
        doc.setString("firstname", "John");
        doc.setString("lastname", "Doe");

        byte[] data = "CouchbaseLite Database".getBytes(StandardCharsets.UTF_8);
        Blob blob = new Blob("text/plain", new ByteArrayInputStream(data));
        doc.setBlob("blob", blob);

        coll.save(doc);

        Document saved = coll.getDocument(doc.getId());
        assertNotNull(saved);
        assertEquals("John", saved.getString("firstname"));
        assertEquals("Doe", saved.getString("lastname"));
        assertNotNull(saved.getBlob("blob"));
    }
}
