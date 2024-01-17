pipeline {
    agent any

    stages{
        stage{
            steps {
                library identifier: 'custom-lib@master', retriever: modernSCM(
                  [$class: 'GitSCMSource',
                   remote: 'https://github.com/Aleksandr13579/ayml-parse.git'])
            }
        }
        stage('first'){
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