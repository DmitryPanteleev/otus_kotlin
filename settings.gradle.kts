rootProject.name = "home_work"

pluginManagement {

    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val ktorVersion: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-spring-boot-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("homework_1")
include("homework_2")
include("api-v1-jackson")
include("api-v2-kmp")
