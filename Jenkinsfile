pipeline {
  agent any
  stages {
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
  post {
    always {
    subject: "FROM JENKINS: ${env.JOB_NAME} ${currentBuild.currentResult}",
        emailext body: "${env.JOB_NAME} build no. ${env.BUILD_NUMBER} ${currentBuild.currentResult}: \n More info at: ${env.BUILD_URL}",
        recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
    }
  }

}
