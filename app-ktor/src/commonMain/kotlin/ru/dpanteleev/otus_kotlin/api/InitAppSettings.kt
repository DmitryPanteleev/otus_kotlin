package ru.dpanteleev.otus_kotlin.api

import io.ktor.server.application.Application
import ru.dpanteleev.otus_kotlin.AppSettings
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.MgRepoStub
import ru.dpanteleev.otus_kotlin.logging.common.LoggerProvider
import ru.dpanteleev.otus_kotlin.logging.jvm.mgLoggerLogback
import ru.dpanteleev.otus_kotlin.plugins.MgDbType
import ru.dpanteleev.otus_kotlin.plugins.getDatabaseConf

fun Application.initAppSettings(): AppSettings {
	val corSettings = CoreSettings(
		loggerProvider = getLoggerProviderConf(),
		repoTest = getDatabaseConf(MgDbType.TEST),
		repoProd = getDatabaseConf(MgDbType.PROD),
		repoStub = MgRepoStub()
	)
	return AppSettings(
		appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
		processor = MgProcessor(corSettings),
		logger = getLoggerProviderConf(),
	)
}

//fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider { loggerKermit(it) }
fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider { mgLoggerLogback(it) }

//private fun Application.initAppAuth(): AuthConfig = AuthConfig(
//	secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
//	issuer = environment.config.property("jwt.issuer").getString(),
//	audience = environment.config.property("jwt.audience").getString(),
//	realm = environment.config.property("jwt.realm").getString(),
//	clientId = environment.config.property("jwt.clientId").getString(),
//	certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
//)
