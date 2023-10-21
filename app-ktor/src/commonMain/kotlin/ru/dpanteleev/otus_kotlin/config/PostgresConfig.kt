package ru.dpanteleev.otus_kotlin.config

import io.ktor.server.config.ApplicationConfig

data class PostgresConfig(
	val url: String = "jdbc:postgresql://localhost:5432/marketplace",
	val user: String = "postgres",
	val password: String = "marketplace-pass",
	val schema: String = "marketplace",
) {
	constructor(config: ApplicationConfig) : this(
		url = config.let {
			val t = config.toMap()
			config.property("DB_URL").getString()
		},
		user = config.property("$PATH.user").getString(),
		password = config.property("$PATH.password").getString(),
		schema = config.property("$PATH.schema").getString(),
	)

	companion object {
		const val PATH = "repository.psql"
	}
}
