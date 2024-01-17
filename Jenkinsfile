
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    sh 'pwd && ls -alrt'
                    sh 'ls -alrt ./var'
                    def example = load "${env.WORKSPACE}/yaml-parse/var/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}