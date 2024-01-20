import main.groovy.org.example.classes.YamlFile

def call(def jenkins) {
    jenkins.sh "unzip ${params.ARCHIVE_1}"
    jenkins.echo "Hello world"
    jenkins.sh "pwd"
    jenkins.sh "ls -alrt"
    println("hello World!!")

    YamlFile yamlFileFirst = new YamlFile("${params.ARCHIVE_1}")
    YamlFile yamlFileSecond = new YamlFile("${params.ARCHIVE_2}")
    jenkins.echo "yamlFileFirst"
    yamlFileFirst.getData()
    jenkins.echo "yamlFileSecond"
    yamlFileSecond.getData()
}