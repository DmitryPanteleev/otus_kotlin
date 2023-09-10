plugins {
//    kotlin("plugin.serialization")
    kotlin("multiplatform")
}
kotlin {
    jvm {
    }
    linuxX64 {}
//    macosX64 {}
    macosArm64 {}

    sourceSets {
        val logbackVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(project(":common"))

                // v2 api
                api(project(":api-v2-kmp"))
                api(project(":mappers-v2-kmp"))

                // biz
                api(project(":app-biz"))

                // logging
                api(project(":lib-logging-common"))
                api(project(":lib-logging-kermit"))
                api(project(":mappers-logging"))
//                api(project(":api-log1"))

                // Stubs
                implementation(project(":stubs"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
