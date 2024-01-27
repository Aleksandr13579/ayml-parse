package main.groovy.org.example.classes

//package org.example.classes
import org.yaml.snakeyaml.Yaml

class YamlFile {

    YamlFile(File yourFile) {
        this.file = yourFile
        this.fileName = "undefind"
        this.yamlData = yamlFile.load(this.file.text) as Map<String, ?>
    }

    YamlFile(String nameFile) {
        try {
            this.file = new File(nameFile)
            this.fileName = nameFile
            //InputStream input = new FileInputStream(this.file)
            this.yamlFile = new Yaml()
            this.yamlData = yamlFile.load(this.file.text) as Map<String, ?>

        }
        catch (FileNotFoundException e) {
            Echo.jenkEcho("File not found Exeption: " + e)
        }
    }

    Map yamlData

    private File file
    private String fileName
    private Yaml yamlFile

}
