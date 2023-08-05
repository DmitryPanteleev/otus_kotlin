@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val prometeusVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
	"io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
	id("application")
	kotlin("plugin.serialization")
	kotlin("multiplatform")
	id("io.ktor.plugin")
}
dependencies {
	implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-openapi:$ktorVersion")
	implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktorVersion")
	implementation("io.micrometer:micrometer-registry-prometheus:$prometeusVersion")
	implementation("io.ktor:ktor-server-metrics-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-call-id-jvm:$ktorVersion")
}

repositories {
	maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
	mavenCentral()
}

application {
	mainClass.set("ru.dpanteleev.otus_kotlin.ApplicationKt")
}

ktor {
	docker {
		localImageName.set(project.name + "-ktor")
		imageTag.set(project.version.toString())
		jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
	}
}

kotlin {
	jvm {
		withJava()
	}
	linuxX64 {}
//	macosX64 {}
	macosArm64 {}

	targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
		binaries {
			executable {
				entryPoint = "ru.dpanteleev.otus_kotlin.main"
			}
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"				implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
				implementation(ktor("cio")) // "io.ktor:ktor-server-cio:$ktorVersion"
				implementation(ktor("auth")) // "io.ktor:ktor-server-auth:$ktorVersion"
				implementation(ktor("auto-head-response")) // "io.ktor:ktor-server-auto-head-response:$ktorVersion"
				implementation(ktor("caching-headers")) // "io.ktor:ktor-server-caching-headers:$ktorVersion"
				implementation(ktor("cors")) // "io.ktor:ktor-server-cors:$ktorVersion"
//				implementation(ktor("config-yaml")) // "io.ktor:ktor-server-config-yaml:$ktorVersion"
				implementation(ktor("content-negotiation")) // "io.ktor:ktor-server-content-negotiation:$ktorVersion"
				implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"

				implementation(project(":common"))
				implementation(project(":app-biz"))

				// v2 ru.dpanteleev.otus_kotlin.api
				implementation(project(":api-v2-kmp"))
				implementation(project(":mappers-v2-kmp"))

				// Stubs
				implementation(project(":stubs"))

				implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
			}
		}

		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))

				implementation(ktor("test-host"))
				implementation(ktor("content-negotiation", prefix = "client-"))
			}
		}
	}
}

tasks.withType<Copy> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
