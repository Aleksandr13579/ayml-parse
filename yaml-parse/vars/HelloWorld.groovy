import main.groovy.org.example.classes.YamlFile

def call(def jenkins) {
    jenkins.echo "Hello world"
    jenkins.sh "pwd"
    jenkins.sh "ls -alrt ./yaml-parse/resources"
    println("hello World!!")

    YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
    YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

    Compare compare = new Compare(new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}"),
            new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}"))
    jenkins.echo "Compare initialisation"

    println(compare.comprasiion())

    yamlFileFirst.getData().each {
        println(it.getValue())
    }

    jenkins.echo "yamlFileSecond"
    yamlFileSecond.getData().each {
        println(it.getValue())
    }

    println("============")
    println(yamlFileFirst.equals(yamlFileSecond))
}