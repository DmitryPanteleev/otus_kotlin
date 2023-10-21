package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
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
abstract class RepoMgCreateTest {
	abstract val repo: IMgRepository

	protected open val lockNew: MgLock = MgLock("20000000-0000-0000-0000-000000000002")

	private val createObj = Mortgage(
		title = "create object",
		description = "create object description",
		bankId = BankId(1234),
		visibility = Visibility.REGISTERED_ONLY,
		borrowerCategoryModel = BorrowerCategoryModel.SALARY,
	)

	@Test
	fun createSuccess() = runRepoTest {
		val result = repo.createMg(DbMgRequest(createObj))
		val expected = createObj.copy(id = result.data?.id ?: MortgageId.NONE)
		assertEquals(true, result.isSuccess)
		assertEquals(expected.title, result.data?.title)
		assertEquals(expected.description, result.data?.description)
		assertEquals(expected.borrowerCategoryModel, result.data?.borrowerCategoryModel)
		assertNotEquals(MortgageId.NONE, result.data?.id)
		assertEquals(emptyList(), result.errors)
		assertEquals(lockNew, result.data?.lock)
	}

	companion object : BaseInitMg(2000) {
		override val initObjects: List<Mortgage> = emptyList()
	}
}
