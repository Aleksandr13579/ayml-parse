import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {
    YamlFile yamlFileFirst = new YamlFile()
    YamlFile yamlFileSecond = new YamlFile()
    Compare compare = new Compare()

    node {
        timestamps {
            stage('Load filed') {

                yamlFileFirst("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                yamlFileSecond("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                compare(yamlFileFirst, yamlFileSecond, jenkins)

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