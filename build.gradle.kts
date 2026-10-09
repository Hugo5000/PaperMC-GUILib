plugins {
    id("at.hugob.common") version "+"
    id("at.hugob.publish") version "+"
}

repositories {
    // paper-api
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // paper api
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

publish {
    developer {
        name.set("Hugo")
        email.set("Hugo")
    }
}