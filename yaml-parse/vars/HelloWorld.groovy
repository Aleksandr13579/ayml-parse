import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {
    node {
        timestamps {
            stage('Exapmle') {

                YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

                yamlFileFirst.getData().each { println(it) }
                println("+++++++++++++++++++++++++++++")
                yamlFileSecond.getData().each { println(it) }

                Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
                jenkins.echo "Compare initialisation"

                println(compare.comprasion(this))


                echo "============"
                println(yamlFileFirst.equals(yamlFileSecond))
            }
        }
    }
}