import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.Echo

def call(def jenkins) {
    new Echo(jenkins)


    node {
        timestamps {
            stage('Load filed') {

                YamlFile yamlFileFirst = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                YamlFile yamlFileSecond = new YamlFile("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                Comparecompare = new Compare(yamlFileFirst, yamlFileSecond)


            }
        }
    }
}