pipeline {
  agent none
  stages {
    stage('BUILD') {
      steps {
        echo '"Test"'
      }
    }
    // for testing failed
    stage('Email Notification'){
      mail bcc: '', body: '''Hi Welcome to jenkins email alerts
      Thanks
      Hari''', cc: '', from: '', replyTo: '', subject: 'Jenkins Job', to: '123@gmail.com'
   }

  }
}