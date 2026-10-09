rootProject.name = "GUILib"

pluginManagement {
    repositories {
        var dir = rootProject.projectDir
        while (dir.parentFile != null &&
            dir.resolve("local-repo").exists().not()) {
            dir = dir.parentFile
        }
        val repo = dir.resolve("local-repo")
        println("Plugin repository: ${repo.absolutePath}")
        maven(url = repo.toURI())
        gradlePluginPortal()
    }
}