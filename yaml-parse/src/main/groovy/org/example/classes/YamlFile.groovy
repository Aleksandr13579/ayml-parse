package main.groovy.org.example.classes

import org.yaml.snakeyaml.Yaml

class YamlFile {

    YamlFile(File yourFile) {
        this.file = yourFile
        this.fileName = "undefind"
        this.data = yamlFile.load(this.file.text)
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
            this.file = new File(nameFile)
            this.fileName = nameFile
            //InputStream input = new FileInputStream(this.file)
            this.yamlFile = new Yaml()
            this.data = yamlFile.load(this.file.text)
        }
        catch (FileNotFoundException e) {
            println("File not found Exeption: " + e)
        }
    }

     def getData() {
        return this.data
    }

    boolean equals(YamlFile ya) {
        return this.data == ya.getData()
    }

    private File file
    private String fileName
    private Yaml yamlFile
    private def data
}
