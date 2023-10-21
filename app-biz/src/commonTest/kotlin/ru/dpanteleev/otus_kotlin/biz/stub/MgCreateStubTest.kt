package ru.dpanteleev.otus_kotlin.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

@OptIn(ExperimentalCoroutinesApi::class)
class MgCreateStubTest {

	private val processor = MgProcessor()
	val id = MortgageId("666")
	val title = "title 666"
	val description = "desc 666"
	val borrowerCategoryModel = BorrowerCategoryModel.SALARY
	val visibility = Visibility.PUBLIC

	@Test
	fun create() = runTest {

		val ctx = Context(
			command = Command.CREATE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.SUCCESS,
			mortgageRequest = Mortgage(
				id = id,
				title = title,
				description = description,
				borrowerCategoryModel = borrowerCategoryModel,
				visibility = visibility,
			),
		)
		processor.exec(ctx)
		assertEquals(Stub.get().id, ctx.mortgageResponse.first().id)
		assertEquals(title, ctx.mortgageResponse.first().title)
		assertEquals(description, ctx.mortgageResponse.first().description)
		assertEquals(borrowerCategoryModel, ctx.mortgageResponse.first().borrowerCategoryModel)
		assertEquals(visibility, ctx.mortgageResponse.first().visibility)
	}

	@Test
	fun badTitle() = runTest {
		val ctx = Context(
			command = Command.CREATE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_TITLE,
			mortgageRequest = Mortgage(
				id = id,
				title = "",
				description = description,
				borrowerCategoryModel = borrowerCategoryModel,
				visibility = visibility,
			),
		)
		processor.exec(ctx)
		assertEquals("title", ctx.errors.firstOrNull()?.field)
		assertEquals("validation", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun badDescription() = runTest {
		val ctx = Context(
			command = Command.CREATE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_DESCRIPTION,
			mortgageRequest = Mortgage(
				id = id,
				title = title,
				description = "",
				borrowerCategoryModel = borrowerCategoryModel,
				visibility = visibility,
			),
		)
		processor.exec(ctx)
		assertEquals("description", ctx.errors.firstOrNull()?.field)
		assertEquals("validation", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun databaseError() = runTest {
		val ctx = Context(
			command = Command.CREATE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.DB_ERROR,
			mortgageRequest = Mortgage(
				id = id,
			),
		)
		processor.exec(ctx)
		assertEquals("internal", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun badNoCase() = runTest {
		val ctx = Context(
			command = Command.CREATE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_ID,
			mortgageRequest = Mortgage(
				id = id,
				title = title,
				description = description,
				borrowerCategoryModel = borrowerCategoryModel,
				visibility = visibility,
			),
		)
		processor.exec(ctx)
		assertEquals("stub", ctx.errors.firstOrNull()?.field)
		assertEquals("validation", ctx.errors.firstOrNull()?.group)
	}
}
