//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        getDataFromYaml(firstYaml, secondYaml)
    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

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
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        getDataFromYaml(firstYaml, secondYaml)
    }

    /**
     * сроавнгиваем информацию из двух ямл файлов
     */
    private String comprasiion() {
        StringBuilder unComprasionString
        if (dataFromFirstFile.size() > dataFromSecondFile.size()) {
            dataFromFirstFile.each { key, value ->
                if (!dataFromSecondFile.containsKey(key)) {
                    unComprasionString.append(key + " " + value + "\n")
                }
            }
            return unComprasionString
        }
        else if (dataFromFirstFile == dataFromSecondFile) {
            return "The first file is equivalent to the second"
        }
    }

    @NonCPS
    private void getDataFromYaml(def firstYamlTMP, def secondYamlTMP) {
        this.dataFromFirstFile = firstYamlTMP.getData() as LinkedHashMap<String, String>
        this.dataFromSecondFile = secondYamlTMP.getData() as LinkedHashMap<String, String>
    }

    private YamlFile firstYaml
    private YamlFile secondYaml
    private ArrayList<String>mismatchedFields
    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
