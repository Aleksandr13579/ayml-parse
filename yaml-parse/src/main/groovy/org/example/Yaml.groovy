package org.example

import org.example.classes.*
import static org.example.classes.Constants.*


ParseYaml yamlFileFirst = new ParseYaml(PATH_TO_FILE_1)
ParseYaml yamlFileSecond = new ParseYaml(PATH_TO_FILE_2)
def tttt = new Yaml().l
if (yamlFileFirst.getData() == yamlFileSecond.getData()) {
}