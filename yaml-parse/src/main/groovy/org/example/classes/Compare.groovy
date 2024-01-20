package org.example.classes

class Compare {

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()
    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()
    }

    Compare(String first, String second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()
    }


    private firstYaml
    private secondYaml
    private mismatchedFields
    private dataFromFirstFile
    private dataFromSecondFile
}
