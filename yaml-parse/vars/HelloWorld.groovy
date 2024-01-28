import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.Echo

def call(def jenkins) {
    new Echo(jenkins)
    YamlFile yamlFileFirst
    YamlFile yamlFileSecond
    Compare compare

    node {
        timestamps {
            stage('Load filed') {

                yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                compare = new Compare(yamlFileFirst, yamlFileSecond)

                jenkins.echo "${compare.dataFromFirstFile}"

                compare.dataFromSecondFile.each { key, value ->
                    jenkins.echo "${key} : ${value}"
                }


            }
        }
    }
}