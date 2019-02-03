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
    stage('Analysis') {
      steps {
        sh 'mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd'
      }
    }
    stage('') {
      steps {
        jacoco(buildOverBuild: true, changeBuildStatus: true)
      }
    }
  }
  post {
    always {
      junit '**/target/surefire-reports/TEST-*.xml'
      recordIssues(enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc()])
      recordIssues(enabledForFailure: true, tool: checkStyle())
      recordIssues(enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml'))
      recordIssues(enabledForFailure: true, tool: pmd(pattern: '**/target/pmd.xml'))

    }

  }
}