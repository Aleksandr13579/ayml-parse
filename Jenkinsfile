
pipeline {
    agent any

    stages{
        stage('first'){
            steps {
                script {
                    sh 'pwd && ls -alrt'
                    def example = load "${env.WORKSPACE}/vars/HelloWorld.groovy"
                    example.HelloWorld()
                }
            }
        }
    }
}