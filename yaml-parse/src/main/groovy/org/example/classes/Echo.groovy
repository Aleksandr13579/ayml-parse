package main.groovy.org.example.classes

class Echo {

    Echo(def jenkins) {
        this.jenkins = jenkins
    }

   jenkEcho(String info) {
        this.jenkins.echo "${info}"
    }

    private def jenkins
}
