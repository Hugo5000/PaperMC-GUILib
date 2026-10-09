plugins {
    id("at.hugob.common") version "+"
    id("at.hugob.publish") version "+"
}

repositories {
    mavenCentral()
    // paper-api
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // paper api
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks{
    test {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed")
        }
    }
}

publish {
    developer {
        name = "Hugo"
        email = "noreply@hugob.at"
    }
}