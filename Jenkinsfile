
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    def example = load "${env.WORKSPACE}/var/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}