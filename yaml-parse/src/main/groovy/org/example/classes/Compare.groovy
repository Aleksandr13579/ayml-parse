package org.example.classes

class Compare {

    Compare(ParseYaml first, ParseYaml second) {
        this.firstYaml = first
        this.secondYaml = second
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()
    }

    Compare(File first, File second) {
        this.firstYaml = new ParseYaml(first)
        this.secondYaml = new ParseYaml(second)
        this.mismatchedFields = new ArrayList<>()
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()
    }

    Compare(String first, String second) {
        this.firstYaml = new ParseYaml(first)
        this.secondYaml = new ParseYaml(second)
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
