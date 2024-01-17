
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    sh 'pwd'
                    def example = load "${env.WORKSPACE}/var/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}