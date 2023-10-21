package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.repo.DbMgRequest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoMgUpdateTest {
	abstract val repo: IMgRepository
	protected open val updateSucc = initObjects[0]
	protected open val updateConc = initObjects[1]
	protected val updateIdNotFound = MortgageId("I'm random uuid)")
	protected val lockBMg = MgLock("20000000-0000-0000-0000-000000000009")
	protected val lockNew = MgLock("20000000-0000-0000-0000-000000000002")

	private val reqUpdateSucc by lazy {
		Mortgage(
			id = updateSucc.id,
			title = "update object",
			description = "update object description",
			bankId = BankId(1000),
			visibility = Visibility.REGISTERED_ONLY,
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			lock = initObjects.first().lock,
		)
	}
	private val reqUpdateNotFound = Mortgage(
		id = updateIdNotFound,
		title = "update object not found",
		description = "update object not found description",
		bankId = BankId(1000),
		visibility = Visibility.REGISTERED_ONLY,
		borrowerCategoryModel = BorrowerCategoryModel.SALARY,
		lock = initObjects.first().lock,
	)
	private val reqUpdateConc by lazy {
		Mortgage(
			id = updateConc.id,
			title = "update object not found",
			description = "update object not found description",
			bankId = BankId(1000),
			visibility = Visibility.REGISTERED_ONLY,
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			lock = lockBMg,
		)
	}

	@Test
	fun updateSuccess() = runRepoTest {
		val result = repo.updateMg(DbMgRequest(reqUpdateSucc))
		assertEquals(true, result.isSuccess)
		assertEquals(reqUpdateSucc.id, result.data?.id)
		assertEquals(reqUpdateSucc.title, result.data?.title)
		assertEquals(reqUpdateSucc.description, result.data?.description)
		assertEquals(reqUpdateSucc.borrowerCategoryModel, result.data?.borrowerCategoryModel)
		assertEquals(emptyList(), result.errors)
	}

	@Test
	fun updateNotFound() = runRepoTest {
		val result = repo.updateMg(DbMgRequest(reqUpdateNotFound))
		assertEquals(false, result.isSuccess)
		assertEquals(null, result.data)
		val error = result.errors.find { it.code == "not-found" }
		assertEquals("id", error?.field)
	}

	@Test
	fun updateConcurrencyError() = runRepoTest {
		val result = repo.updateMg(DbMgRequest(reqUpdateConc))
		assertEquals(false, result.isSuccess)
		val error = result.errors.find { it.code == "concurrency" }
		assertEquals("lock", error?.field)
		assertEquals(updateConc, result.data)
	}

	companion object : BaseInitMg(2000) {
		override val initObjects: List<Mortgage> = listOf(
			createInitTestModel(100),
			createInitTestModel(200),
		)
	}
}
