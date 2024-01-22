package main.groovy.org.example.classes
//package org.example.classes
import org.yaml.snakeyaml.Yaml

class YamlFile {

    YamlFile(File yourFile) {
        this.file = yourFile
        this.fileName = "undefind"
        this.yamlData = yamlFile.load(this.file.text) as LinkedHashMap<String, String>
        /**
        try {
            InputStream input = new FileInputStream(this.file)
            this.yamlFile = new Yaml()
            this.data = yamlFile.loadAll(input).split(", ")
        }
        catch (Exception e) {
            println(e)
        }
         */
    }

    YamlFile(String nameFile, def script) {
        try {
            this.file = new File(nameFile)
            this.fileName = nameFile
            //InputStream input = new FileInputStream(this.file)
            this.yamlFile = new Yaml()
            this.yamlData = yamlFile.load(this.file.text) as LinkedHashMap<String, String>
        }
        catch (FileNotFoundException e) {
            println("File not found Exeption: " + e)
        }
        script.echo "pvhjeriuhvjpreihjuveorijhvepriouvhjerpiovjerpjoerpio"
    }

     @NonCPS
     def getData() {
        return this.yamlData
    }

    boolean equals(YamlFile ya) {
        return this.yamlData == ya.getData()
    }

    private File file
    private String fileName
    private Yaml yamlFile
    private def yamlData
}
