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

                StringBuilder report = new StringBuilder()
                String struct = new String()

                stage('Chekout') {
                    git(
                            url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                            branch: "main"
                    )
                }
                stage('Unzip files') {
                    sh (script: "mkdir ${env.WORKSPACE}/yaml-parse/resources/first", returnStdout: false)
                    sh (script:  "mkdir ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: false)
                    sh (script:  "unzip ${env.WORKSPACE}/yaml-parse/resources/first.zip -d ${env.WORKSPACE}/yaml-parse/resources/first", returnStdout: false)
                    sh (script:  "unzip ${env.WORKSPACE}/yaml-parse/resources/second.zip -d ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: false)
                    struct = sh (script:  "tree ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: true)

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
                            echo "Файл ${it} существет в двух архивах"
                            files.add("${it}")
                            report.append("Файл <font color=\"red\">${it}</font> существет в двух архивах<br>")
                        } else {
                            echo "Файл ${it} не существует в старой версии, добавлен в новой"
                            newFiles.add("${it}")
                            report.append("Файл <font color=\"red\">${it}</font> не существует в старой версии, добавлен в новой<br>")
                        }
                    }

                    filesInFirstArchive.each {
                        if (!filesInSecondArchive.contains(it)) {
                            echo "Файл <font color=\"red\">${it}</font> удален из нового архива"
                            deletedFiles.add("${it}")
                            report.append("Файл <font color=\"red\">${it}</font> удален из нового архива<br>")
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
                        report.append("<br><font color=\"green\">Файл: ${it}</font> ${changes}")
                        echo "==============\n"

                    }
                }
                stage('mail') {
                    emailext( to: 'test@mailhog.local',
                            body: "${report}<br>${struct}",
                            subject: "Результат сравнения",
                            mimeType: 'text/html',
                            attachmentsPattern: "**/yaml-parse/resources/f*.zip" )
                }
            } catch (exeption) {
                throw exeption
            } finally {
                cleanWs()
            }
        }
    }
}