package ru.dpanteleev.otus_kotlin.biz.validation

import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.MgRepoStub
import ru.dpanteleev.otus_kotlin.in_memory.MgRepoInMemory
import ru.dpanteleev.otus_kotlin.models.Command

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationOffersTest {

	private val command = Command.OFFERS
	private val processor = MgProcessor(CoreSettings(repoTest = MgRepoStub()))

	@Test
	fun correctId() = validationIdCorrect(command, processor)
	@Test
	fun trimId() = validationIdTrim(command, processor)
	@Test
	fun emptyId() = validationIdEmpty(command, processor)
	@Test
	fun badFormatId() = validationIdFormat(command, processor)

}

