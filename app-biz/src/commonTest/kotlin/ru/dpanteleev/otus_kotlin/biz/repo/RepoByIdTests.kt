package ru.dpanteleev.otus_kotlin.biz.repo

import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.MgRepositoryMock
import ru.dpanteleev.otus_kotlin.biz.addTestPrincipal
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse

private val initAd = Mortgage(
	id = MortgageId("123"),
	title = "abc",
	description = "abc",
	borrowerCategoryModel = BorrowerCategoryModel.SALARY,
	visibility = Visibility.PUBLIC,
)
private val repo = MgRepositoryMock(
	invokeReadAd = {
		if (it.id == initAd.id) {
			DbMgResponse(
				isSuccess = true,
				data = initAd,
			)
		} else DbMgResponse(
			isSuccess = false,
			data = null,
			errors = listOf(MgError(message = "Not found", field = "id"))
		)
	}
)

private val settings by lazy {
	CoreSettings(
		repoTest = repo
	)
}
private val processor by lazy { MgProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: Command) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = MortgageId("12345"),
			title = "xyz",
			description = "xyz",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.REGISTERED_ONLY,
			lock = MgLock("123-234-abc-ABC"),

			),
	)
	ctx.addTestPrincipal()
	processor.exec(ctx)
	assertEquals(State.FAILING, ctx.state)
	assertEquals(Mortgage(), ctx.mortgageResponse.first())
	assertEquals(1, ctx.errors.size)
	assertEquals("id", ctx.errors.first().field)
}
