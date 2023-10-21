package ru.dpanteleev.otus_kotlin.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.FilterRequest
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

@OptIn(ExperimentalCoroutinesApi::class)
class MgSearchStubTest {

	private val processor = MgProcessor()
	val filter = FilterRequest(searchString = "sber")

	@Test
	fun read() = runTest {

		val ctx = Context(
			command = Command.SEARCH,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.SUCCESS,
			filterRequest = filter,
		)
		processor.exec(ctx)
		assertTrue(ctx.mortgageResponse.size > 1)
		val first = ctx.mortgageResponse.firstOrNull() ?: fail("Empty response list")
		assertTrue(first.title.contains(filter.searchString))
		assertTrue(first.description.contains(filter.searchString))
		with(Stub.get()) {
			assertEquals(borrowerCategoryModel, first.borrowerCategoryModel)
			assertEquals(visibility, first.visibility)
		}
	}

	@Test
	fun badId() = runTest {
		val ctx = Context(
			command = Command.SEARCH,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_ID,
			filterRequest = filter,
		)
		processor.exec(ctx)
		assertEquals("id", ctx.errors.firstOrNull()?.field)
		assertEquals("validation", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun databaseError() = runTest {
		val ctx = Context(
			command = Command.SEARCH,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.DB_ERROR,
			filterRequest = filter,
		)
		processor.exec(ctx)
		assertEquals("internal", ctx.errors.firstOrNull()?.group)
	}

	@Test
	fun badNoCase() = runTest {
		val ctx = Context(
			command = Command.SEARCH,
			state = State.NONE,
			workMode = WorkMode.STUB,
			stubCase = MgStubs.BAD_TITLE,
			filterRequest = filter,
		)
		processor.exec(ctx)
		assertEquals("stub", ctx.errors.firstOrNull()?.field)
	}
}
