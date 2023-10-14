package ru.dpanteleev.otus_kotlin.biz.repo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.MgRepositoryMock
import ru.dpanteleev.otus_kotlin.biz.addTestPrincipal
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {

	private val bankId = BankId(321)
	private val command = Command.UPDATE
	private val initAd = Mortgage(
		id = MortgageId("123"),
		title = "abc",
		description = "abc",
		bankId = bankId,
		borrowerCategoryModel = BorrowerCategoryModel.SALARY,
		visibility = Visibility.PUBLIC,
	)
	private val repo by lazy {
		MgRepositoryMock(
			invokeReadAd = {
				DbMgResponse(
					isSuccess = true,
					data = initAd,
				)
			},
			invokeUpdateAd = {
				DbMgResponse(
					isSuccess = true,
					data = Mortgage(
						id = MortgageId("123"),
						title = "xyz",
						description = "xyz",
						borrowerCategoryModel = BorrowerCategoryModel.SALARY,
						visibility = Visibility.OWNER_ONLY,
					)
				)
			}
		)
	}
	private val settings by lazy {
		CoreSettings(
			repoTest = repo
		)
	}
	private val processor by lazy { MgProcessor(settings) }

	@Test
	fun repoUpdateSuccessTest() = runTest {
		val mgToUpdate = Mortgage(
			id = MortgageId("123"),
			title = "xyz",
			description = "xyz",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.OWNER_ONLY,
			lock = MgLock("123-234-abc-ABC"),
		)
		val ctx = Context(
			command = command,
			state = State.NONE,
			workMode = WorkMode.TEST,
			mortgageRequest = mgToUpdate,
		)
		ctx.addTestPrincipal(bankId)
		processor.exec(ctx)
		assertEquals(State.FINISHING, ctx.state)
		assertEquals(mgToUpdate.id, ctx.mortgageResponse.first().id)
		assertEquals(mgToUpdate.title, ctx.mortgageResponse.first().title)
		assertEquals(mgToUpdate.description, ctx.mortgageResponse.first().description)
		assertEquals(mgToUpdate.borrowerCategoryModel, ctx.mortgageResponse.first().borrowerCategoryModel)
		assertEquals(mgToUpdate.visibility, ctx.mortgageResponse.first().visibility)
	}

	@Test
	fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
