@SharedLibrary('yaml-parse/vars') _

pipeline {
    agent any

    stages{
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