package ru.dpanteleev.otus_kotlin.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ru.dpanteleev.otus_kotlin.AppSettings
import ru.dpanteleev.otus_kotlin.api.create
import ru.dpanteleev.otus_kotlin.api.delete
import ru.dpanteleev.otus_kotlin.api.read
import ru.dpanteleev.otus_kotlin.api.search
import ru.dpanteleev.otus_kotlin.api.update

fun Application.configureRouting(appSettings: AppSettings) {
	val processor = appSettings.processor
	routing {
		get("/") {
			call.respondText("Hello My World!")
		}
		route("api") {
			route("v2") {
				post("create") { call.create(processor) }
				post("read") { call.read(processor) }
				post("update") { call.update(processor) }
				post("delete") { call.delete(processor) }
				post("search") { call.search(processor) }
			}
		}
	}
}
