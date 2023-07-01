rootProject.name = "home_work"

include("homework_1")

pluginManagement {
    val kotlinJvmVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinJvmVersion apply false
    }

}
