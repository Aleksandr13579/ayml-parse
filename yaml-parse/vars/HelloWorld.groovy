def call(def jenkins) {
    jenkins.sh "unzip ${ARCHIVE_NAME_1}"
    jenkins.echo "Hello world"
    jenkins.sh "pwd"
    jenkins.sh "ls -alrt"
    println("hello World!!")
}