import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {

    node {
        timestamps {
            stage('Load filed') {

                YamlFile yamlFileFirst = new YamlFile()
                yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")

                YamlFile yamlFileSecond = new YamlFile()
                yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

                Compare compare = new Compare(yamlFileFirst, yamlFileSecond, jenkins)
                def changes = compare.whatHasBeenAdded()
                jenkins.echo "${changes}"


            }
            post {
                always {
                    deleteDir()
                }
            }
        }
    }
}