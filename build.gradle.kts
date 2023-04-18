plugins {
    id("java")
    id("idea")
    id("maven-publish")
}

val githubUsername: String by project
val githubToken: String by project

val version: String by project
val group: String by project
val artifact: String by project
project.group = group
project.version = version
repositories {
    mavenCentral()
    // paper-api
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    // paper api
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}
sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("resources")
        }
    }
    test {
        java {
            srcDir("test")
        }
    }
}
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


tasks.register<Copy>("prepareServer") {
    dependsOn("build")
    from(tasks.jar.get().archiveFile.get().asFile.path)
    rename(tasks.jar.get().archiveFile.get().asFile.name, "${project.name}.jar")
    into("G:\\paper\\plugins")
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }
    compileTestJava { options.encoding = "UTF-8" }
    javadoc { options.encoding = "UTF-8" }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group
            artifactId = artifact
            version = version

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Hugo5000/PaperMC-GUILib")
            credentials {
                username = githubUsername
                password = githubToken
            }
        }
    }
}