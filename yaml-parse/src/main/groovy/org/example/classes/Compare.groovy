//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        getDataFromYaml(firstYaml, secondYaml)
    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        println("uuuuuu")
        getDataFromYaml(firstYaml, secondYaml)
    }

    Compare(String first, String second) {
        try {
            this.firstYaml = new YamlFile(first)
            this.secondYaml = new YamlFile(second)
        }
        catch (FileNotFoundException e) {
            println("File not found: \n" + e)
        }
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        println("uuuuuu")
        getDataFromYaml(firstYaml, secondYaml)
    }

    /**
     * сроавнгиваем информацию из двух ямл файлов
     */
    LinkedHashMap<String, String> comprasion(def script) {
        StringBuilder returnString
        if (dataFromFirstFile.size() > dataFromSecondFile.size()) {
            dataFromFirstFile.each { key, value ->
                if (!dataFromSecondFile.containsKey(key) || value != dataFromSecondFile.get(key)) {
                    script.echo "Тренировки оккупились 1 "
                    returnString.append(key + " " + value + "\n")
                }
            }
            return returnString.toString()

        } else if (dataFromFirstFile.size() < dataFromSecondFile.size()) {
                dataFromFirstFile.each { key, value ->
                    if (!dataFromSecondFile.containsKey(key) || value != dataFromSecondFile.get(key)) {
                        script.echo "Тренировки оккупились 2 "
                        returnString.append(key + " " + value + "\n")
                    }
                }
                return returnString.toString()
        } else if (dataFromFirstFile == dataFromSecondFile) {
            println("The first file is equivalent to the second")
        }
    }

    @NonCPS
    private void getDataFromYaml(def firstYamlTMP, def secondYamlTMP) {
        this.dataFromFirstFile = firstYamlTMP.getData() as LinkedHashMap<String, String>
        this.dataFromSecondFile = secondYamlTMP.getData() as LinkedHashMap<String, String>

        dataFromFirstFile.each {println(it)}

        println("==============================")

        dataFromSecondFile.each {println(it)}
    }

    private YamlFile firstYaml
    private YamlFile secondYaml
    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
