plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
//    macosX64 {}
    macosArm64 {}

    sourceSets {
        val coroutinesVersion: String by project

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":common"))
                implementation(project(":stubs"))
                implementation(project(":lib-biz-core"))
                implementation(project(":repo-in-memory"))
                implementation(project(":repo-tests"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":repo-in-memory"))
                implementation(project(":repo-tests"))
                implementation(project(":repo-stubs"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
    }
}
