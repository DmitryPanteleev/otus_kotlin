package ru.dpanteleev.otus_kotlin.plugins

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.CachingOptions
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureHTTP() {
	install(CachingHeaders) {
		options { call, outgoingContent ->
			when (outgoingContent.contentType?.withoutParameters()) {
				ContentType.Application.Json -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
				else -> null
			}
		}
	}

	install(CORS) {
		allowMethod(HttpMethod.Options)
		allowMethod(HttpMethod.Put)
		allowMethod(HttpMethod.Delete)
		allowMethod(HttpMethod.Patch)
		allowMethod(HttpMethod.Get)
	}
}
