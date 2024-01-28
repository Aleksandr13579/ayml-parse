//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second, def jenkins) {
        this.firstYaml = first
        this.secondYaml = second

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(jenkins,firstYaml.getData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(jenkins, secondYaml.getData(), this.dataFromSecondFile)

    }

    Compare(File first, File second, def jenkins) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(jenkins,firstYaml.getData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(jenkins, secondYaml.getData(), this.dataFromSecondFile)

    }

    Compare(String first, String second, def jenkins) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(jenkins,firstYaml.getData(), this.dataFromFirstFile)


        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(jenkins, secondYaml.getData(), this.dataFromSecondFile)

    }

    /**
     * метод для преобразования загруженного yaml файла в LinkedHashMap вида
     * key1:
     *   key2:
     *     key3: value
     *
     * в key1.key2.key3: value
     *
     * @param yam
     * @param oldKey сохрянем значения ключей предыдущих итераций
     * @return
     */
    @NonCPS
    private void converter(def jenkins, Map<String, ?> yam, Map<String, String> data, String oldKey = "") {
        yam.each { key, value ->
            if (key == null) key = "2222222222"
            if (value == null) value = "2222222222"
            if (value instanceof Map) {
                if (oldKey != "") {
                    converter(jenkins,value, data, "${oldKey}.${key}")
                } else {
                    converter(jenkins, value, data, "${key}")
                }
            } else if (value instanceof ArrayList) {
                value.each {
                    jenkins.echo "${it.getClass().getSimpleName();}"
                    if (it instanceof String) {

                        jenkins.echo "${oldKey}.${key} : ${value.toString()}"
                        data.put(oldKey + "." + key, value.toString())
                    } else {
                        converter(jenkins, it, data, "${oldKey}.${key}")
                    }
                }
            } else {
                if (oldKey != "") {
                    jenkins.echo "${oldKey}.${key} : ${value.toString()}"
                    data.put(oldKey + "." + key, value.toString())
                } else {
                    jenkins.echo "${key} : ${value.toString()}"
                    data.put(key, value.toString())
                }
            }
        }
    }

    @NonCPS
    String whatHasBeenAdded(def jenkins) {
        LinkedHashMap<String, String> differentValue = new LinkedHashMap<>()
        StringBuilder differentKey
        StringBuilder allChanges
        this.dataFromFirstFile.each {key,value ->
            if (this.dataFromSecondFile.containsKey(key)) {
                jenkins.echo "2222222222"
                if (value != this.dataFromSecondFile.get(key)) {
                    jenkins.echo "${key} : ${value.toString()}"
                    jenkins.echo "${this.dataFromSecondFile.get(key)}"
                    differentValue.put(key, this.dataFromSecondFile.get(key))
                }

            } else {
                jenkins.echo "${key}"
                differentKey.append(key + ", ")
            }
        }
        allChanges.append("Различия по ключам: ${differentKey.length() != 0 ? differentKey : "Различий нет"} \n")
                .append("Отличия по значениям \n")
        if (!differentValue.isEmpty()) {
            differentValue.each {key, value ->
                allChanges.append(key + " : " + value + "\n")
            }
        }
        return allChanges
    }

    private YamlFile firstYaml
    private YamlFile secondYaml
    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
