plugins {
    kotlin("jvm")
    java
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // common
    implementation(project(":common"))
    implementation(project(":app-common"))

    // v2 api
    implementation(project(":mappers-v2-kmp"))
    implementation(project(":api-v2-kmp"))

    implementation(project(":app-biz"))
    // other
//    implementation(project(":ok-marketplace-lib-logging-logback"))

    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation(kotlin("test"))
    testImplementation(project(":stubs"))
}


