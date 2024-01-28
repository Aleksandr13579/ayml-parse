package main.groovy.org.example.classes

//package org.example.classes
import org.yaml.snakeyaml.Yaml

class YamlFile {

    YamlFile(File yourFile) {
        this.file = yourFile
        this.fileName = "undefind"
    }

    YamlFile(String nameFile) {
        try {
            this.file = new File(nameFile)
            this.fileName = nameFile
            this.yamlFile = new Yaml()

        }
        catch (FileNotFoundException e) {
            Echo.jenkEcho("File not found Exeption: " + e)
        }
    }

    @NonCPS
    load(File file ) {
        this.yamlData = yamlFile.load(this.file.text)
    }

    private Map yamlData

    private File file
    private String fileName
    private Yaml yamlFile

}
