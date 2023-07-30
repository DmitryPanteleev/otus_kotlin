package ru.dpanteleev.otus_kotlin

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import ru.dpanteleev.otus_kotlin.plugins.configureHTTP
import ru.dpanteleev.otus_kotlin.plugins.configureRouting
import ru.dpanteleev.otus_kotlin.plugins.configureSerialization

fun main() {
	embeddedServer(
		CIO,
		port = 8080,
		host = "0.0.0.0",
		module = Application::module)
		.start(wait = true)
}

//fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
	configureHTTP()
//	configureMonitoring()
	configureSerialization()
//	configureDatabases()
	configureRouting()
}
