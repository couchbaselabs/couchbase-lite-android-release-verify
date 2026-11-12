pipeline {
  agent none

  parameters {
    string(name: 'CBL_VERSION', defaultValue: '4.0.2', description: 'Couchbase Lite version')
  }

  environment {
    // Pixel 4 XL (mob-e2e-android-01). The host has more than one device and
    // the connected tests would otherwise run on all of them.
    ANDROID_SERIAL = '1BCACS00001MX3'
  }

  stages {
    stage('Verify downloads') {
    agent { label 'mob-e2e-android-01' }
        options {
            lock("mob-e2e-android-01")
        }
      steps {
        checkout scm
        sh '''
          set -euo pipefail
          chmod +x verify_android.sh
          ./verify_android.sh "$CBL_VERSION"
        '''
      }
    }

    stage('Enterprise tests (EE + EE-KTX)') {
    agent { label 'mob-e2e-android-01' }
        options {
            lock("mob-e2e-android-01")
        }
      steps {
        checkout scm
        sh '''
          set -euo pipefail
          chmod +x run_cbl_test.sh
          ./run_cbl_test.sh "$CBL_VERSION" couchbase-lite-android-ee couchbase-lite-android-ee-ktx
        '''
      }
    }

    stage('Community tests (CE + CE-KTX)') {
    agent { label 'mob-e2e-android-01' }
        options {
            lock("mob-e2e-android-01")
        }
      steps {
        checkout scm
        sh '''
          set -euo pipefail
          chmod +x run_cbl_test.sh
          ./run_cbl_test.sh "$CBL_VERSION" couchbase-lite-android couchbase-lite-android-ktx
        '''
      }
    }
  }
}
