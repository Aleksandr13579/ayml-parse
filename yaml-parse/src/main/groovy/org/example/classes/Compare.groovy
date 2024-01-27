//package org.example.classes
package main.groovy.org.example.classes

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second, def jenkins) {
        this.firstYaml = first
        this.secondYaml = second

        this.dataFromFirstFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(firstYaml.getYamlData(), jenkins)

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData(), jenkins)
    }
/**
    Compare(File first, File second) {
        this.firstYaml = new YamlFile(first)
        this.secondYaml = new YamlFile(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(firstYaml.getYamlData())

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData())
    }
*/
    Compare(String first, String second, def jenkins) {
        try {
            this.firstYaml = new YamlFile(first)
            this.secondYaml = new YamlFile(second)
        }
        catch (FileNotFoundException e) {
            println("File not found: \n" + e)
        }

        this.dataFromFirstFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(firstYaml.getYamlData(), jenkins)

        this.dataFromSecondFile = new LinkedHashMap<>()
        dataFromSecondFile = converter(secondYaml.getYamlData(), jenkins)

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
    private LinkedHashMap<String, String> converter(Map<String, ?> yam, def jenkins, String oldKey = "") {
        LinkedHashMap<String, String> data
        jenkins.echo "vsdvdsvsdvsdvsdvsdvsdvsdvsd"
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
                        jenkins.echo "${oldKey}.${key} : ${value.toString()}"
                        data.put(oldKey + "." + key, it.toString())
                    } else {
                        converter(it, "${oldKey}.${key}")
                    }
                }
            } else {
                if (oldKey != "") {
                    jenkins.echo "${oldKey}.${key} : ${value.toString()}"
                    data.put(oldKey + "." + key, value.toString())
                } else {
                    jenkins.echo(${key} : ${value.toString()})
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
