//package org.example.classes
package main.groovy.org.example.classes


class Compare {

    LinkedHashMap<String, String> mismatchedFields

    Compare(Map<String, ?> first, Map<String, ?> second) {

        this.dataFromFirstFile = new LinkedHashMap<>()
        converter(first, this.dataFromFirstFile)

        this.dataFromSecondFile = new LinkedHashMap<>()
        converter(second, this.dataFromSecondFile)
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
                int count = 0
                if (oldKey != "") {
                    converter(value, data, "${oldKey}.${key}")
                } else {
                    converter(value, data, "${key}")
                }
            } else if (value instanceof ArrayList) {
                int count = 0
                value.each {
                    if (it instanceof String) {
                        data.put(oldKey + "." + key + "[${count++}]", value.toString())
                    } else {
                        converter(it, data, "${oldKey}.${key}.[${count++}]")
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
                newKey.append("<br>" + key)
            }
        }

        allChanges.append("<br><font color=\"#0044CC\">Добавлены параметры:</font> ${newKey.length() != 0 ? "<font color=\"#FF1919\">${newKey}</font><br>" : "Различий нет<br>"} \n")
                .append("<font color=\"#0044CC\">Изменения в параметрах по значениям:</font> <br>")

        this.dataFromFirstFile.each {key,value ->
            if (!this.dataFromSecondFile.containsKey(key)) {
                oldKey.append('\n' + key)
            }
        }

        if (!differentValue.isEmpty()) {
            differentValue.each {key, value ->
                allChanges.append("В параметре " + "<font color=\"#FF1919\">${key}</font>" + " было "
                        + "<font color=\"#FF1919\">${this.dataFromFirstFile.get(key)}</font>" + " стало " + "<font color=\"#FF1919\">${value}</font>" + '<br>')
            }
        }
        allChanges.append("${oldKey.size() > 0 ? "<font color=\"#B3BFFF\"Удалены параметры:</font><br>" : "Удаленных параметров нет<br>"}").append(oldKey)

        return allChanges
    }

    private LinkedHashMap<String, String> dataFromFirstFile
    private LinkedHashMap<String, String> dataFromSecondFile
}
