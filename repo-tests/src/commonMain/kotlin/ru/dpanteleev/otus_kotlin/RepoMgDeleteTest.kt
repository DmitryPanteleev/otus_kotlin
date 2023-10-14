package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoMgDeleteTest {
	abstract val repo: IMgRepository
	protected open val deleteSucc = initObjects[0]
	protected open val deleteConc = initObjects[1]
	protected open val notFoundId = MortgageId("1234321")

	@Test
	fun deleteSuccess() = runRepoTest {
		val lockOld = deleteSucc.lock
		val result = repo.deleteMg(DbMgIdRequest(deleteSucc.id, lock = lockOld))

		assertEquals(true, result.isSuccess)
		assertEquals(emptyList(), result.errors)
		assertEquals(lockOld, result.data?.lock)
	}

	@Test
	fun deleteNotFound() = runRepoTest {
		val result = repo.readMg(DbMgIdRequest(notFoundId, lock = lockOld))

		assertEquals(false, result.isSuccess)
		assertEquals(null, result.data)
		val error = result.errors.find { it.code == "not-found" }
		assertEquals("id", error?.field)
	}

	@Test
	fun deleteConcurrency() = runRepoTest {
		val lockOld = deleteSucc.lock
		val result = repo.deleteMg(DbMgIdRequest(deleteConc.id, lock = lockBad))

		assertEquals(false, result.isSuccess)
		val error = result.errors.find { it.code == "concurrency" }
		assertEquals("lock", error?.field)
		assertEquals(lockOld, result.data?.lock)
	}

	companion object : BaseInitMg(12341234) {
		override val initObjects: List<Mortgage> = listOf(
			createInitTestModel(1234),
			createInitTestModel(2345),
		)
	}
}
