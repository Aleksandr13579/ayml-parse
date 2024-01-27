import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {
    node {
        timestamps {
            stage('Example') {

                YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                Compare compare = new Compare(yamlFileFirst, yamlFileSecond)

                compare.getDataFromFirstFile().each { key, value ->
                    jenkins.echo "${key} : ${value}"
                }

                compare.getDataFromSecondFile().each { key, value ->
                    jenkins.echo "${key} : ${value}"
                }
            }
        }
    }
}