pipeline {
  agent any

  parameters {
    string(name: 'CBL_VERSION', defaultValue: '4.0.2', description: 'Couchbase Lite version')
  }

  stages {
    stage('Verify downloads') {
      steps {
        sh '''
          set -euo pipefail
          chmod +x verify_download.sh
          ./verify_download.sh "$CBL_VERSION"
        '''
      }
    }

    stage('Enterprise tests (EE + EE-KTX)') {
      steps {
        sh '''
          set -euo pipefail
          chmod +x run_cbl_test.sh
          ./run_cbl_test.sh "$CBL_VERSION" couchbase-lite-android-ee couchbase-lite-android-ee-ktx
        '''
      }
    }

    stage('Community tests (CE + CE-KTX)') {
      steps {
        sh '''
          set -euo pipefail
          chmod +x run_cbl_test.sh
          ./run_cbl_test.sh "$CBL_VERSION" couchbase-lite-android couchbase-lite-android-ktx
        '''
      }
    }
  }
}
