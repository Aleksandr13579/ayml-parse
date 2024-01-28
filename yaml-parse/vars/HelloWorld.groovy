import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {

    node {
        timestamps {
            stage('chekout') {
                git(
                        url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                        branch: "main"
                )
            }
            stage('Load filed') {

                YamlFile yamlFileFirst = new YamlFile()
                yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")

                YamlFile yamlFileSecond = new YamlFile()
                yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

                Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
                def changes = compare.whatHasBeenAdded( )
                jenkins.echo "${changes}"


            }
            deleteDir()
        }
    }
}