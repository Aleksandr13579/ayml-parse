package org.example

import org.example.classes.*
import static org.example.classes.Constants.*


YamlFile yamlFileFirst = new YamlFile(${params.ARCHIVE_1})
YamlFile yamlFileSecond = new YamlFile(${params.ARCHIVE_2})
def tttt = new Yaml().l
if (yamlFileFirst.getData() == yamlFileSecond.getData()) {
}