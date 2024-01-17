def example = load "${env.WORKSPACE}/var/HelloWorld.groovy"

pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    example.HelloWorld()
                }
            }
        }
    }
}