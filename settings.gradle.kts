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
        id("com.bmuschko.docker-remote-ru.dpanteleev.otus_kotlin.api") version bmuschkoVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("homework_1")
include("homework_2")
//include("ru.dpanteleev.otus_kotlin.api-v1-jackson")
include("api-v2-kmp")
include("mappers-v2-kmp")
include("mappers-logging")
include("common")
include("app-ktor")
include("app-biz")
include("stubs")
include("app-rabbit")
include("app-common")
include("lib-logging-common")
include("lib-logging-kermit")
include("lib-biz-core")
