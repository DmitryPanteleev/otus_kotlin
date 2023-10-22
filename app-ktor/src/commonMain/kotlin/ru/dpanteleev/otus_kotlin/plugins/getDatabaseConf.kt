package ru.dpanteleev.otus_kotlin.plugins

import io.ktor.server.application.Application
import ru.dpanteleev.otus_kotlin.config.PostgresConfig
import ru.dpanteleev.otus_kotlin.postgress.RepoMgSQL
import ru.dpanteleev.otus_kotlin.postgress.SqlProperties
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

fun Application.getDatabaseConf(type: MgDbType): IMgRepository = when (type) {
	MgDbType.PROD -> initPostgresDev()
	MgDbType.TEST -> initPostgresDev()
}


enum class MgDbType(val confName: String) {
	PROD("prod"), TEST("test")
}

private fun Application.initPostgres(): IMgRepository {
	val config = PostgresConfig(environment.config)
	return RepoMgSQL(
		properties = SqlProperties(
			url = config.url,
			user = config.user,
			password = config.password,
			schema = config.schema,
		)
	)
}

private fun Application.initPostgresDev(): IMgRepository = RepoMgSQL(properties = SqlProperties())

