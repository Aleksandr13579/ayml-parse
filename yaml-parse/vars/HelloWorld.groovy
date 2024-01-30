import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

LinkedHashSet<String> filesInFirstArchive =  new LinkedHashSet<>()
LinkedHashSet<String> filesInSecondArchive = new LinkedHashSet<>()

def fileAndPathInFirstArchive = [:]
def fileAndPathInSecondArchive = [:]

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

                    filesInFirstArchive = sh ( script: "find ${env.WORKSPACE}/yaml-parse/resources/first -name '*.yaml'", returnStdout: true )
                    filesInSecondArchive = sh ( script: "find ${env.WORKSPACE}/yaml-parse/resources/second -name '*.yaml'",returnStdout: true )

                    echo "${filesInFirstArchive}"
                    echo "+++++++++++++++++++++"
                    echo "${filesInSecondArchive}"
                }
                stage('') {
                    def parser1 = ~/.*first\/(.*)\/(.*)$/
                    def parser2 = ~/.*second\/(.*)\/(.*)$/

                    filesInFirstArchive.each {
                        if ((match = it =~ parser1))
                            fileAndPathInFirstArchive.put(match.group(2), match(1))
                    }

                    filesInSecondArchive.each {
                        if ((match = it =~ parser2))
                            fileAndPathInSecondArchive.put(match.group(2), match(1))
                    }

                    fileAndPathInFirstArchive.each { jenkins.echo "${it}"}
                    fileAndPathInSecondArchive.each { jenkins.echo "${it}"}
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