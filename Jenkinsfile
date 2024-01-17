
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    sh 'pwd && ls -alrt'
                    sh 'ls -alrt ./var'
                    def example = load "${env.WORKSPACE}/var/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}