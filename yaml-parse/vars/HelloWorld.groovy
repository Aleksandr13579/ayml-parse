import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.Echo

LinkedHashSetSet<String> filesInFirstArchive =  new LinkedHashSet<>()
LinkedHashSet<String> filesInSecondArchive = new LinkedHashSet<>()

def call(def jenkins) {
    new Echo(jenkins)

    node {
        timestamps {
            stage('Chekout') {
                git(
                        url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                        branch: "main"
                )
            }
            stage('Unzip files') {
                unzip zipFile: "${env.WORKSPACE}/yaml-parse/resources/first.zip", dir: "${env.WORKSPACE}/yaml-parse/resources/first"
                unzip zipFile: "${env.WORKSPACE}/yaml-parse/resources/second.zip", dir: "${env.WORKSPACE}/yaml-parse/resources/second"

                sh "ls -alrt ${env.WORKSPACE}/yaml-parse/resources"
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