grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.plugin.repos.distribution.myRepo ="https://github.com/IntelliGrape/Grails-Google-Plus-Plugin.git"

// use 'grails maven-deploy' to deploy plugins to the FreqRepo maven2 repository:
grails.project.repos.FreqRepo.url = "http://jira.frequency.com/maven2/repo"
grails.project.dependency.distribution = {
    localRepository = "/var/www/maven2/repo"
    remoteRepository(id: "FreqRepo", url: "scp://jira.frequency.com/var/www/maven2/repo") {
        authentication username: "root", privateKey: "${userHome}/.ssh/id_rsa-frequency"
    }
}
grails.project.repos.default = "FreqRepo"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
//        grailsRepo "https://github.com/IntelliGrape/Grails-Google-Plus-Plugin.git"
        grailsRepo "http://grails.org/plugins"
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

    plugins {
        build(":tomcat:$grailsVersion") {
            export = false
        }
    }
}
