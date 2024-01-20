def call(def jenkins) {
    jenkins.echo "Hello world"
    jenkins.sh "pwd"
    jenkins.sh "ls -alrt"
    println("hello World!!")
}