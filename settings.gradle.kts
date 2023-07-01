rootProject.name = "home_work"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }

}

//include("homework_1")
include("homework_2")
