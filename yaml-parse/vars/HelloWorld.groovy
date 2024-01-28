import main.groovy.org.example.classes.YamlFile
import main.groovy.org.example.classes.Compare
import main.groovy.org.example.classes.Echo

def call(def jenkins) {
    new Echo(jenkins)
    YamlFile yamlFileFirst
    YamlFile yamlFileSecond
    Compare compare

    node {
        timestamps {
            stage('Load filed') {

                yamlFileFirst = new YamlFile()
                yamlFileFirst.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_1}")
                jenkins.echo  "vsdsdvsddsv"
                yamlFileSecond = new YamlFile()
                yamlFileSecond.load("${env.WORKSPACE}/yaml-parse/resources/${params.ARCHIVE_2}")
                jenkins.echo "vsdvsdvsdvs x xcxcxczzxc"
                compare = new Compare(yamlFileFirst, yamlFileSecond)
                def changes = compare.whatHasBeenAdded()
                jenkins.echo "${changes}"
                jenkins.echo "yyyyyyyy"

            }
        }
    }
}