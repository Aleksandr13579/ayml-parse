package org.example.classes

class Echo {

    Echo(def jenkins) {
        this.jenkins = jenkins
    }

   static echo(String info) {
        this.jenkins.echo "${info}"
    }

    private def jenkins
}
