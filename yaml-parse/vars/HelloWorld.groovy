import main.groovy.org.example.classes.YamlFile

def call(def jenkins) {
    jenkins.echo "Hello world"
    jenkins.sh "pwd"
    jenkins.sh "ls -alrt ./yaml-parse/resources"
    println("hello World!!")

    YamlFile yamlFileFirst = new YamlFile("./yaml-parse/resources/${params.ARCHIVE_1}")
    YamlFile yamlFileSecond = new YamlFile("./yaml-parse/resources/${params.ARCHIVE_2}")
    jenkins.echo "yamlFileFirst"
    println(yamlFileFirst.getData())
    jenkins.echo "yamlFileSecond"
    println(yamlFileSecond.getData())
}