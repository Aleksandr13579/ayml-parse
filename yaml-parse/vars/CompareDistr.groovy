@Grab('com.itextpdf:kernel:7.2.0')
@Grab('com.itextpdf:html2pdf:5.0.0')
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.PDFConverter

def call() {

    node {
        timestamps {
            try {

                Map<String, String> filesInFirstArchive = new LinkedHashMap()
                Map<String, String> filesInSecondArchive = new LinkedHashMap()

                Map<String, String> files = new LinkedHashMap()
                Map<String, String> newFiles = new LinkedHashMap()
                List<String> deletedFiles = new ArrayList<>()

                StringBuilder report = new StringBuilder()

                stage('Chekout') {
                    git(
                            url: 'https://github.com/Aleksandr13579/ayml-parse.git',
                            branch: "main "
                    )
                }
                stage('Unzip files') {
                    sh (script: "mkdir ${env.WORKSPACE}/yaml-parse/resources/first", returnStdout: false)
                    sh (script:  "mkdir ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: false)
                    sh (script:  "unzip ${env.WORKSPACE}/yaml-parse/resources/first.zip -d ${env.WORKSPACE}/yaml-parse/resources/first", returnStdout: false)
                    sh (script:  "unzip ${env.WORKSPACE}/yaml-parse/resources/second.zip -d ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: false)

                    report.append("""
                            <table width = "100%" border = "1">
                            <colgroup><col span=2 style="background-color:#d6d6d6"></colgroup>
                            <tr><th>Дистрибутив</th><th>Версия</th></tr>
                            <tr><td><font size="2">${params.ARCHIVE_1}</font></td><td><font size="2">версия</font></td></tr>
                            <tr><td><font size="2">${params.ARCHIVE_2}</font></td><td><font size="2">версия</font></td></tr>
                            </table><br><br>
                            """
                    )

                    StringBuilder structOld = new StringBuilder()
                    StringBuilder structNew = new StringBuilder()

                    def tree1 = sh (script:  "tree ${env.WORKSPACE}/yaml-parse/resources/first", returnStdout: true).split('\n')
                    tree1.each { structOld.append("${it}<br>")}

                    def tree2 = sh (script:  "tree ${env.WORKSPACE}/yaml-parse/resources/second", returnStdout: true).split('\n')
                    tree2.each { structNew.append("${it}<br>")}

                    report.append("""
                            <table width = "100%" border = "1">
                            <colgroup><col span=2 style="background-color:#d6d6d6"></colgroup>
                            <tr><th>
                                <font size="3" face="times new roman">
                                    Структура ${params.ARCHIVE_1}
                                </font>
                            </th><th>
                                <font size="3" face="times new roman">
                                    Структура ${params.ARCHIVE_2}
                                </font>
                            </th></tr>
                            <tr><td><font size="1">${structOld}</font></td>
                            <td><font size="1">${structNew}</font></td></tr>
                            </table><br><br>
                            """
                    )

                    def firstArchiveUnzip = sh(script: "find ${env.WORKSPACE}/yaml-parse/resources/first -name \"*.yaml\"", returnStdout: true).split('\n')
                    def secondArchiveUnzip = sh(script: "find ${env.WORKSPACE}/yaml-parse/resources/second -name \"*.yaml\"", returnStdout: true).split('\n')

                    firstArchiveUnzip.each { fileName ->
                        def match = fileName =~ "/.*first/(.*)/(.*yaml)/"
                        if (match.find()) filesInFirstArchive.put(match.group(2),match.group(1) + "/")

                    }

                    secondArchiveUnzip.each { fileName ->
                        def match = fileName =~ "/.*second/(.*)/(.*yaml)/"
                        if (match.find()) filesInSecondArchive.put(match.group(2),match.group(1) + "/")
                    }


                    filesInSecondArchive.each { key, value ->
                        if (filesInFirstArchive.containsKey(key)) {
                            files.put(key, value)
                            report.append("Файл <font color=\"#158000\">${key}</font> существует в двух архивах<br>")
                        } else {
                            newFiles.put(key, value)
                            report.append("Файл <font color=\"#158000\">${key}</font> не существует в старой версии, добавлен в новой<br>")
                        }
                    }

                    filesInFirstArchive.each {
                        if (!filesInSecondArchive.containsKey(it)) {
                            deletedFiles.add("${it}")
                            report.append("Файл <font color=\"#158000\">${it}</font> удален из нового архива<br>")
                        }


                    }
                }
                stage('Parse Yaml') {

                    files.each { key, value ->
                        def data1 = readYaml file: "${env.WORKSPACE}/yaml-parse/resources/first/${filesInFirstArchive.get(key)}${key}"

                        echo "${data1.getClass().getSimpleName()}"

                        def data2 = readYaml file: "${env.WORKSPACE}/yaml-parse/resources/second/${value}${key}"

                        Compare compare = new Compare(data1, data2)
                        def changes = compare.whatHasBeenAdded()
                        report.append("<br><font color=\"#158000\">Файл: ${it}</font> ${changes}")

                    }

                    PDFConverter pdfConverter = new PDFConverter()
                    pdfConverter.fromHtmlToPdfConverter(report.toString(), "report.pdf")
                }
                stage('mail') {
                    emailext( to: 'test@mailhog.local',
                            body: "${report}<br>",
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