package ru.dpanteleev.otus_kotlin

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

	@Test
	fun `root endpoint`() = testApplication {
		this.application{
			this.module()
		}
		val response = client.get("/")
		assertEquals(HttpStatusCode.OK, response.status)
		assertEquals("Hello My World!", response.bodyAsText())
	}
}
