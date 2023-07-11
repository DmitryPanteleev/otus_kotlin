package test.action.v1

import io.kotest.assertions.withClue
import io.kotest.matchers.should
import fixture.client.Client

suspend fun Client.searchBank(): Unit =
    withClue("searchBank") {
        val response = sendAndReceive(
            "bank/search", """
                {
                    "name": "Sber"
                }
            """.trimIndent()
        )

        response should haveNoErrors
    }
