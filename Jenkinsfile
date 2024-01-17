pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                library identifier: 'custom-lib', retriever: modernSCM(
                  [$class: 'GitSCMSource',
                   remote: 'https://github.com/Aleksandr13579/ayml-parse.git'])
            }
        }
        stage('second'){
            steps {
                script {
                    sh 'pwd && ls -alrt'
                    sh 'ls -alrt ./yaml-parse'
                    sh 'ls -alrt ./yaml-parse/vars'

                }
            }
        }
    }
}