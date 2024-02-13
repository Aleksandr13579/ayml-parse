@Grab('com.itextpdf:kernel:7.2.0')
@Grab('com.itextpdf:html2pdf:5.0.0')
import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.PDFConverter

def call() {

    node {
        timestamps {
            try {

                List<String> filesInFirstArchive = new ArrayList<>()
                List<String> filesInSecondArchive = new ArrayList<>()

                List<String> files = new ArrayList<>()
                List<String> newFiles = new ArrayList<>()
                List<String> deletedFiles = new ArrayList<>()

                StringBuilder report = new StringBuilder()
                StringBuilder struct = new StringBuilder()

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
                    def tree = sh (script:  "tree ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: true).split('\n')
                    tree.each { struct.append("${it}<br>")}

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
                            files.add("${it}")
                            report.append("Файл <font color=\"#00E64D\">${it}</font> существет в двух архивах<br>")
                        } else {
                            newFiles.add("${it}")
                            report.append("Файл <font color=\"#00E64D\">${it}</font> не существует в старой версии, добавлен в новой<br>")
                        }
                    }

                    filesInFirstArchive.each {
                        if (!filesInSecondArchive.contains(it)) {
                            deletedFiles.add("${it}")
                            report.append("Файл <font color=\"#00E64D\">${it}</font> удален из нового архива<br>")
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
                        report.append("<br><font color=\"#CCFFDD\">Файл: ${it}</font> ${changes}")


                    }

                    PDFConverter pdfConverter = new PDFConverter()
                    pdfConverter.fromHtmlToPdfConverter(report.toString(), "report.pdf")
                }
                stage('mail') {
                    emailext( to: 'test@mailhog.local',
                            body: "${report}<br>${struct}",
                            subject: "Результат сравнения",
                            mimeType: 'text/html',
                            attachmentsPattern: "**/yaml-parse/resources/f*.zip" )
                }
            } catch (exeption) {
                throw new Exception(exeption)
            } finally {
                cleanWs()
            }
        }
    }
}