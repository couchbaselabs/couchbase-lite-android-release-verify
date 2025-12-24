package com.example.cblverify

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.couchbase.lite.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream

@RunWith(AndroidJUnit4::class)
class BasicCblKtxInstrumentedTest {

    private val dbName = "release-verification-ktx"
    private var db: Database? = null

    @Before
    fun setUp() {
        val ctx: Context = ApplicationProvider.getApplicationContext()
        CouchbaseLite.init(ctx)

        safeDeleteDb()
        db = Database(dbName, DatabaseConfiguration())
    }

    @After
    fun tearDown() {
        try { db?.close() } catch (_: Exception) {}
        db = null
        safeDeleteDb()
    }

    private fun safeDeleteDb() {
        try {
            if (Database.exists(dbName, null)) Database.delete(dbName, null)
        } catch (_: CouchbaseLiteException) {}
    }

    @Test
    fun basicDatabaseAndBlobTestKotlin() {
        Log.i("CBL-VERIFY", "Running flavor=" + BuildConfig.CBL_FLAVOR)
        assertEquals("ktx", BuildConfig.CBL_FLAVOR)
        val database = requireNotNull(db)

        val coll = database.defaultCollection
        val doc = MutableDocument()
            .setString("firstname", "John")
            .setString("lastname", "Doe")

        val data = "CouchbaseLite Database".encodeToByteArray()
        val blob = Blob("text/plain", ByteArrayInputStream(data))
        doc.setBlob("blob", blob)

        coll.save(doc)

        val saved = coll.getDocument(doc.id)
        requireNotNull(saved)
        assertEquals("John", saved.getString("firstname"))
        assertEquals("Doe", saved.getString("lastname"))
        requireNotNull(saved.getBlob("blob"))
    }
}
