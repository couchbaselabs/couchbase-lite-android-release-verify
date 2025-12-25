pipeline {
  agent none

  parameters {
    string(name: 'CBL_VERSION', defaultValue: '4.0.2', description: 'Couchbase Lite version')
  }

  stages {
    stage('Verify downloads') {
        checkout scm
        agent { label 'mob-e2e-mac-01' }
            options {
                lock("mob-e2e-android-01")
            }
      steps {
        sh '''
          set -euo pipefail
          chmod +x "$WORKSPACE/verify_download.sh"
          "$WORKSPACE/verify_download.sh" "$CBL_VERSION"
        '''
      }
    }

    stage('Enterprise tests (EE + EE-KTX)') {
        checkout scm
        agent { label 'mob-e2e-mac-01' }
            options {
                lock("mob-e2e-android-01")
            }
      steps {
        sh '''
          set -euo pipefail
          chmod +x "$WORKSPACE/run_cbl_test.sh"
          "$WORKSPACE/run_cbl_test.sh" "$CBL_VERSION" couchbase-lite-android-ee couchbase-lite-android-ee-ktx
        '''
      }
    }

    stage('Community tests (CE + CE-KTX)') {
        checkout scm
        agent { label 'mob-e2e-mac-01' }
            options {
                lock("mob-e2e-android-01")
            }
      steps {
        sh '''
          set -euo pipefail
          chmod +x "$WORKSPACE/run_cbl_test.sh"
          "$WORKSPACE/run_cbl_test.sh" "$CBL_VERSION" couchbase-lite-android couchbase-lite-android-ktx
        '''
      }
    }
  }
}
