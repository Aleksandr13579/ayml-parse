def example = load "${WORKSPACE}/var/HelloWorld.groovy"

pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                example.HelloWorld()
            }
        }
    }
}