import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare

def call(def jenkins) {

    node {
        timestamps {
            stage('Load filed') {

                YamlFile yamlFileFirst = new YamlFile()
                yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                jenkins.echo  "vsdsdvsddsv"
                YamlFile yamlFileSecond = new YamlFile()
                yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                jenkins.echo "vsdvsdvsdvs x xcxcxczzxc"
                Compare compare = new Compare(yamlFileFirst, yamlFileSecond)
                def changes = compare.whatHasBeenAdded()
                jenkins.echo "${changes}"
                jenkins.echo "yyyyyyyy"

            }
        }
    }
}