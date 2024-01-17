
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    sh 'pwd && ls -alrt'
                    sh 'ls -alrt ./yaml-parse'
                    sh 'ls -alrt ./yaml-parse/vars'
                    def example = load "${env.WORKSPACE}/vars/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}