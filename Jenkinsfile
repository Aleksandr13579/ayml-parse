library(
    identifier: 'yaml-parse@main',
    retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/Aleksandr13579/ayml-parse.git']))

pipeline {
    agent any

    stages{
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