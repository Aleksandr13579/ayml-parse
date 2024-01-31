//package org.example.classes
package main.groovy.org.example.classes

import main.groovy.org.example.classes.Echo

class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(YamlFile first, YamlFile second) {
        this.firstYaml = first
        this.secondYaml = second

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getData(), this.dataFromSecondFile)

    }

    Compare(File first, File second) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getData(), this.dataFromSecondFile)

    }

    Compare(String first, String second) {
        this.firstYaml = new YamlFile()
        firstYaml.load(first)
        this.secondYaml = new YamlFile()
        secondYaml.load(second)

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(firstYaml.getData(), this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(secondYaml.getData(), this.dataFromSecondFile)
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
    private void converter(Map<String, ?> yam, Map<String, String> data, String oldKey = "") {
        yam.each { key, value ->
            if (value instanceof Map) {
                if (oldKey != "") {
                    converter(value, data, "${oldKey}.${key}")
                } else {
                    converter(value, data, "${key}")
                }
            } else if (value instanceof ArrayList) {
                value.each {
                    if (it instanceof String) {
                        data.put(oldKey + "." + key, value.toString())
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
    String whatHasBeenAdded( ) {
        LinkedHashMap<String, String> differentValue = new LinkedHashMap<>()
        StringBuilder newKey = new StringBuilder()
        StringBuilder oldKey = new StringBuilder()
        StringBuilder allChanges = new StringBuilder()
        this.dataFromSecondFile.each {key,value ->
            if (this.dataFromFirstFile.containsKey(key)) {
                if (value != this.dataFromFirstFile.get(key)) {
                    differentValue.put(key, this.dataFromSecondFile.get(key))
                }
            } else {
                newKey.append('\n' + key)
            }
        }

        allChanges.append("<font color=\"blue\">Добавлены параметры:</font> ${newKey.length() != 0 ? "<font color=\"red\">${newKey}</font>" + "<br>" : "Различий нет<br>"} \n")
                .append("Изменения в параметрах по значениям: <br>")

        this.dataFromFirstFile.each {key,value ->
            if (!this.dataFromSecondFile.containsKey(key)) {
                oldKey.append('\n' + key)
            }
        }

        if (!differentValue.isEmpty()) {
            differentValue.each {key, value ->
                allChanges.append("В параметре " + "<font color=\"red\">${key}</font>" + " было "
                        + "<font color=\"red\">${this.dataFromFirstFile.get(key)}</font>" + " стало " + "<font color=\"red\">${value}</font>" + '<br>')
            }
        }
        allChanges.append("${oldKey.size() > 0 ? "Удалены параметры:<br>" : "Удаленных параметров нет<br>"}").append(oldKey)

        return allChanges
    }

    private YamlFile firstYaml
    private YamlFile secondYaml
    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
