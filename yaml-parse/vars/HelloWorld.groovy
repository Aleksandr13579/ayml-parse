import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {
    node {
        stage('Exapmle') {

            YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
            YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

            Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
            jenkins.echo "Compare initialisation"

            println(compare.comprasion())


            echo "============"
            println(yamlFileFirst.equals(yamlFileSecond))
        }
    }
}