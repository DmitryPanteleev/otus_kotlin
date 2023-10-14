package ru.dpanteleev.otus_kotlin.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

@OptIn(ExperimentalCoroutinesApi::class)
class MgDeleteStubTest {

	private val processor = MgProcessor()
	val id = MortgageId("666")

	@Test
	fun delete() = runTest {

		val ctx = Context(
			command = Command.DELETE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.SUCCESS,
			mortgageRequest = Mortgage(
				id = id,
			),
		)
		processor.exec(ctx)

		val stub = Stub.get()
		assertEquals(stub.id, ctx.mortgageResponse.first().id)
		assertEquals(stub.title, ctx.mortgageResponse.first().title)
		assertEquals(stub.description, ctx.mortgageResponse.first().description)
		assertEquals(stub.borrowerCategoryModel, ctx.mortgageResponse.first().borrowerCategoryModel)
		assertEquals(stub.visibility, ctx.mortgageResponse.first().visibility)
	}

	@Test
	fun badId() = runTest {
		val ctx = Context(
			command = Command.DELETE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_ID,
			mortgageRequest = Mortgage(),
		)
		processor.exec(ctx)
		assertEquals("id", ctx.errors.firstOrNull()?.field)
		assertEquals("validation", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun databaseError() = runTest {
		val ctx = Context(
			command = Command.DELETE,
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
			command = Command.DELETE,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_TITLE,
			mortgageRequest = Mortgage(
				id = id,
			),
		)
		processor.exec(ctx)
		assertEquals("stub", ctx.errors.firstOrNull()?.field)
	}
}
