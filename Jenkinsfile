pipeline {
  agent 'any'
  stages {
    stage('BUILD') {
      steps {
        sh 'mvn clean install package'
      }
    }
   
     stage ('Analysis') {
            steps {
                sh 'mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd jacoco:jacoco'
            }
           
        }
  }
  post {
        always {
            junit testResults: '**/target/surefire-reports/TEST-*.xml'

            recordIssues enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc()]
            recordIssues enabledForFailure: true, tool: checkStyle()
            recordIssues enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml')
            recordIssues enabledForFailure: true, tool: pmd(pattern: '**/target/pmd.xml')
            recordIssues enabledForFailure: true, tool: pmd(pattern: '**/target/site/jacoco/jacoco*.xml')
        }
    }
  
 
} 
