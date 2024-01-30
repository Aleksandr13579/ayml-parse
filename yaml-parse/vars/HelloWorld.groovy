import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare


def call(def jenkins) {

    node {
        timestamps {
            try {

                ArrayList<String> filesInFirstArchive = new ArrayList<>()
                ArrayList<String> filesInSecondArchive = new ArrayList<>()

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

                    def firstArchiveUnzip = sh ( script: "find ${env.WORKSPACE}/yaml-parse/resources/first -name \"*.yaml\"", returnStdout: true )
                    def secondArchiveUnzip = sh ( script: "find ${env.WORKSPACE}/yaml-parse/resources/second -name \"*.yaml\"",returnStdout: true )

                    filesInFirstArchive = firstArchiveUnzip.split('\n')
                    filesInSecondArchive = secondArchiveUnzip.split('\n')


                    echo "${filesInFirstArchive}"
                    echo "+++++++++++++++++++++"
                    echo "${filesInSecondArchive}"
                    echo "============================"

                    filesInSecondArchive.each {
                        if (filesInFirstArchive.contains(it)) {
                            echo "File ${it} exist in first archive"
                        } else {
                            echo "File ${it} do not exist in first archive"
                        }
                    }

                }
                stage('Parse Yaml') {

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