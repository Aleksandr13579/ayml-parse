//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        this.dataFromFirstFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(firstYaml.getYamlData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData())
    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)
        this.dataFromFirstFile = new LinkedHashMap<>()
        this.dataFromSecondFile = new LinkedHashMap<>()

        this.dataFromFirstFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(firstYaml.getYamlData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData())
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
        dataFromSecondFile = converter(firstYaml.getYamlData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData())

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
     * @param oldKey - сохрянем значения ключей пердыдущих итераций
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



    private YamlFile firstYaml
    private YamlFile secondYaml
    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
