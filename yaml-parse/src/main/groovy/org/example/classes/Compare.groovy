//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getYamlData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getYamlData(), this.dataFromSecondFile)
    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getYamlData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getYamlData(), this.dataFromSecondFile)
    }

    Compare(String first, String second) {

        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getYamlData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getYamlData(), this.dataFromSecondFile)

    }

    LinkedHashMap<String, String> dataFromFirstFile
    LinkedHashMap<String, String> dataFromSecondFile

    @NonCPS
    getDataFromFirstFile() {
        return this.dataFromFirstFile
    }

    @NonCPS
    getDataFromSecondFile() {
        return this.dataFromSecondFile
    }

    /**
     * сроавнгиваем информацию из двух ямл файлов
     */

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
    private void converter(Map<String, ?> yam, Map<String, String> data, String oldKey = "") {
        yam.each { key, value ->
            if (value instanceof Map) {
                if (oldKey != "") {
                    converter(value, data, "${oldKey}.${key}")
                } else {
                    converter(value, data, "${key}")
                }
            } else if (value instanceof ArrayList<?>) {
                value.each {
                    if (it instanceof String) {
                        data.put(oldKey + "." + key, it.toString())
                    } else {
                        converter(it, data, "${oldKey}.${key}")
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
    }

    @NonCPS
    String whatHasBeenAdded() {
        LinkedHashMap<String, String> differentValue
        StringBuilder differentKey
        StringBuilder allChanges
        dataFromFirstFile.each {key,value ->
            if (dataFromSecondFile.containsKey(key)) {

                if (value != dataFromSecondFile.get(key)) {
                    differentValue.put(key, dataFromSecondFile.get(key))
                }

            } else {
                differentKey.append(key + ", ")
            }
        }
        allChanges.append("Различия по ключам: ${differentKey.length() != 0 ? differentKey : "Различий нет"} \n")
        allChanges.append("Отличия по значениям \n")
        if (!differentValue.isEmpty()) {
            differentValue.each {key, value ->
                allChanges.append(key + " : " + value + "\n")
            }
        }
        return allChanges
    }

    private YamlFile firstYaml
    private YamlFile secondYaml
}
