import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare


def call(def jenkins) {

    node {
        timestamps {
            try {

                List<String> filesInFirstArchive = new ArrayList<>()
                List<String> filesInSecondArchive = new ArrayList<>()

                List<String> files = new ArrayList<>()
                List<String> newFiles = new ArrayList<>()
                List<String> deletedFiles = new ArrayList<>()

                stage('Chekout') {
                    git(
                            url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                            branch: "main"
                    )
                }
                stage('Unzip files') {
                    sh "mkdir ${env.WORKSPACE}/yaml-parse/resources/first"
                    sh "mkdir ${env.WORKSPACE}/yaml-parse/resources/second"
                    sh " unzip ${env.WORKSPACE}/yaml-parse/resources/first.zip -d ${env.WORKSPACE}/yaml-parse/resources/first > /dev/null"
                    sh " unzip ${env.WORKSPACE}/yaml-parse/resources/second.zip -d ${env.WORKSPACE}/yaml-parse/resources/second > /dev/null"

                    def firstArchiveUnzip = sh(script: "find ${env.WORKSPACE}/yaml-parse/resources/first -name \"*.yaml\"", returnStdout: true).split('\n')
                    def secondArchiveUnzip = sh(script: "find ${env.WORKSPACE}/yaml-parse/resources/second -name \"*.yaml\"", returnStdout: true).split('\n')

                    firstArchiveUnzip.each {
                        def pattern = ~/.*first\/(.*yaml|.*yml)$/

                        def match = pattern.matcher(it)
                        if (match.find()) filesInFirstArchive.add(match.group(1))
                    }

                    secondArchiveUnzip.each {
                        def pattern = ~/.*second\/(.*yaml|.*yml)$/

                        def match = pattern.matcher(it)
                        if (match.find()) filesInSecondArchive.add(match.group(1))
                    }


                    filesInSecondArchive.each {
                        if (filesInFirstArchive.contains(it)) {
                            echo "File ${it} exist in first archive"
                            files.add("${it}")
                        } else {
                            echo "File ${it} do not exist in first archive"
                            newFiles.add("${it}")
                        }
                    }

                    filesInFirstArchive.each {
                        if (!filesInSecondArchive.contains(it)) {
                            echo "File ${it} was deleted from new archive"
                            deletedFiles.add("${it}")
                        }


                    }
                }
                stage('Parse Yaml') {

                    files.each {
                        YamlFile yamlFileFirst = new YamlFile()
                        yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/first/${it}")

                        YamlFile yamlFileSecond = new YamlFile()
                        yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/second/${it}")

                        Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
                        def changes = compare.whatHasBeenAdded()
                        echo "Filename: ${it}"
                        echo "${changes}"
                        echo "==============\n"

                    }


                }
            } catch (exeption) {
                throw exeption
            } finally {
                cleanWs()
            }
        }
    }
}