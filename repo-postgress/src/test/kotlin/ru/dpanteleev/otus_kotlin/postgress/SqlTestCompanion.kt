package ru.dpanteleev.otus_kotlin.postgress

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration
import ru.dpanteleev.otus_kotlin.models.Mortgage

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    private const val SCHEMA = "marketplace"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initObjects: Collection<Mortgage> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoMgSQL {
        return RepoMgSQL(SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects, randomUuid = randomUuid)
    }
}
