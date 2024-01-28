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


    private YamlFile firstYaml
    private YamlFile secondYaml
}
