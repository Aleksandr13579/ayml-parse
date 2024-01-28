import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

LinkedHashSet<String> filesInFirstArchive =  new LinkedHashSet<>()
LinkedHashSet<String> filesInSecondArchive = new LinkedHashSet<>()

def call(def jenkins) {

    node {
        timestamps {
            try {
                stage('Chekout') {
                    git(
                            url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                            branch: "main"
                    )
                }
                stage('Unzip files') {
                    sh "mkdir ${env.WORKSPACE}/yaml-parse/resources/first"
                    sh "mkdir ${env.WORKSPACE}/yaml-parse/resources/second"
                    sh " unzip ${env.WORKSPACE}/yaml-parse/resources/first.zip -d ${env.WORKSPACE}/yaml-parse/resources/first"
                    sh " unzip ${env.WORKSPACE}/yaml-parse/resources/second.zip -d ${env.WORKSPACE}/yaml-parse/resources/second"

                    filesInFirstArchive = sh "find ${env.WORKSPACE}/yaml-parse/resources/first - name '*.yaml'"
                    filesInSecondArchive = sh "find ${env.WORKSPACE}/yaml-parse/resources/second - name '*.yaml'"

                    echo "${filesInFirstArchive}"
                    echo "+++++++++++++++++++++"
                    echo "${filesInSecondArchive}"
                }
                stage('Load filed') {

                    YamlFile yamlFileFirst = new YamlFile()
                    yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")

                    YamlFile yamlFileSecond = new YamlFile()
                    yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")

                    Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
                    def changes = compare.whatHasBeenAdded()
                    jenkins.echo "${changes}"


                }
            } catch (exeption) {
                throw exeption
            } finally {
                cleanWs()
            }
        }
    }
}