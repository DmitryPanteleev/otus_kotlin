plugins {
    kotlin("jvm")
}

allprojects {
    group = "ru.dpanteleev.otus_kotlin"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jetpack.io") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
