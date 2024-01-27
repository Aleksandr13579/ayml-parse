import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {
    node {
        timestamps {
            stage('Example') {

                YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                jenkins.echo ("dddddd")
                YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                jenkins.echo ("ddqwqqwqwqwqw")
                Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
            }
        }
    }
}