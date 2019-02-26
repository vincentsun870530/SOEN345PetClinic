pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install package'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn clean test'
      }
    }
    stage('Analysis') {
      steps {
        sh 'mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd'
        jacoco(buildOverBuild: true, changeBuildStatus: true)
      }
    }
  }
}
