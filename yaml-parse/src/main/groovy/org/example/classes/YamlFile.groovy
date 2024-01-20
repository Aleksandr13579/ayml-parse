package main.groovy.org.example.classes

import org.yaml.snakeyaml.Yaml

class YamlFile {

    YamlFile(File yourFile) {
        this.file = yourFile.text
        this.fileName = "undefind"
        this.yamlData = yamlFile.load(this.file as InputStream)
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

    YamlFile(String nameFile) {
        try {
            this.file = new File(nameFile).text
            this.fileName = nameFile
            //InputStream input = new FileInputStream(this.file)
            this.yamlFile = new Yaml()
            this.yamlData = yamlFile.load(this.file as InputStream)
            println(yamlData)
        }
        catch (FileNotFoundException e) {
            println("File not found Exeption: " + e)
        }
    }

     def getData() {
        return this.yamlData
    }

    boolean equals(YamlFile ya) {
        return this.data == ya.getData()
    }

    private File file
    private String fileName
    private Yaml yamlFile
    private def yamlData
}
