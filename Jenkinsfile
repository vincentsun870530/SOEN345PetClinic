pipeline {
  agent none
  stages {
    // for testing failed
    stage('Email Notification'){
      mail bcc: '', body: '''Hi Welcome to jenkins email alerts
      Thanks
      Hari''', cc: '', from: '', replyTo: '', subject: 'Jenkins Job', to: '123@gmail.com'
    }
    stage('BUILD') {
      steps {
        sh 'mvn clean install package'
      }
    }
    stage('TEST') {
      steps {
        sh 'mvn clean test'
      }
    }
  }
}