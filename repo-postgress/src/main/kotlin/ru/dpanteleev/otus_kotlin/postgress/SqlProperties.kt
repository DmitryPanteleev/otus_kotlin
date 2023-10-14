package ru.dpanteleev.otus_kotlin.postgress

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/mortgage",
    val user: String = "postgres",
    val password: String = "postgress",
    val schema: String = "mortgage",
    // Delete tables at startup - needed for testing
    val dropDatabase: Boolean = false,
)
