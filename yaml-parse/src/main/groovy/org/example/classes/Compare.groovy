//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second

        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromFirstFile = converter(firstYaml.getData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        this.dataFromSecondFile = converter(secondYaml.getData())

    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromFirstFile = converter(firstYaml.getData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        this.dataFromSecondFile = converter(secondYaml.getData())

    }

    Compare(String first, String second) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromFirstFile = converter(firstYaml.getData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        this.dataFromSecondFile = converter(secondYaml.getData())

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
    private LinkedHashMap<String, String> converter(Map<String, ?> yam, String oldKey = "") {
        LinkedHashMap<String, String> data
        yam.each { key, value ->
            if (value instanceof Map) {
                if (oldKey != "") {
                    converter(value, "${oldKey}.${key}")
                } else {
                    converter(value, "${key}")
                }
            } else if (value instanceof ArrayList<?>) {
                value.each {
                    if (it instanceof String) {
                        data.put(oldKey + "." + key, it.toString())
                    } else {
                        converter(it, "${oldKey}.${key}")
                    }
                }
            } else {
                if (oldKey != "") {
                    data.put(oldKey + "." + key, value.toString())
                } else {
                    data.put(key, value.toString())
                }
            }
        }
        return data
    }

    @NonCPS
    String whatHasBeenAdded() {
        LinkedHashMap<String, String> differentValue
        StringBuilder differentKey
        StringBuilder allChanges
        this.dataFromFirstFile.each {key,value ->
            if (this.dataFromSecondFile.containsKey(key)) {

                if (value != this.dataFromSecondFile.get(key)) {
                    differentValue.put(key, this.dataFromSecondFile.get(key))
                }

            } else {
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
