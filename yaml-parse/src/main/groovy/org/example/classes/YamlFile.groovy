package main.groovy.org.example.classes

//package org.example.classes
import org.yaml.snakeyaml.Yaml

class YamlFile {

    @NonCPS
    load(String nameFile) {
        try {
            this.file = new File(nameFile)
            this.fileName = nameFile
            this.yamlFile = new Yaml()
            this.yamlData = yamlFile.load(this.file.text) as Map
        }
        catch (FileNotFoundException e) {
            Echo.jenkEcho("File not found Exeption: " + e)
        }
    }

    @NonCPS
    load(File file) {
        this.file = yourFile
        this.fileName = "undefind"
        this.yamlData = yamlFile.load(this.file.text) as Map
    }
    @NonCPS
    def getData() {
        return this.yamlData
    }

    private Map yamlData

    private File file
    private String fileName
    private Yaml yamlFile

}
