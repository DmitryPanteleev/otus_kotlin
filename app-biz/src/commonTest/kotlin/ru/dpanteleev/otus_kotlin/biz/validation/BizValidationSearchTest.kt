package ru.dpanteleev.otus_kotlin.biz.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.in_memory.MgRepoInMemory
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.FilterRequest
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

	private val command = Command.SEARCH
	private val processor = MgProcessor(CoreSettings(repoTest = MgRepoInMemory()))

	@Test
	fun correctEmpty() = runTest {
		val ctx = Context(
			command = command,
			state = State.NONE,
			workMode = WorkMode.TEST,
			filterRequest = FilterRequest()
		)
		processor.exec(ctx)
		assertEquals(0, ctx.errors.size)
		assertNotEquals(State.FAILING, ctx.state)
	}
}

