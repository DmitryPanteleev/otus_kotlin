package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoMgReadTest {
	abstract val repo: IMgRepository
	protected open val readSucc = initObjects[0]

	@Test
	fun readSuccess() = runRepoTest {
		val result = repo.readMg(DbMgIdRequest(readSucc.id))

		assertEquals(true, result.isSuccess)
		assertEquals(readSucc, result.data)
		assertEquals(emptyList(), result.errors)
	}

	@Test
	fun readNotFound() = runRepoTest {
		val result = repo.readMg(DbMgIdRequest(notFoundId))

		assertEquals(false, result.isSuccess)
		assertEquals(null, result.data)
		val error = result.errors.find { it.code == "not-found" }
		assertEquals("id", error?.field)
	}

	companion object : BaseInitMg(1234) {
		override val initObjects: List<Mortgage> = listOf(
			createInitTestModel(4321)
		)

		val notFoundId = MortgageId("1234567890")

	}
}
