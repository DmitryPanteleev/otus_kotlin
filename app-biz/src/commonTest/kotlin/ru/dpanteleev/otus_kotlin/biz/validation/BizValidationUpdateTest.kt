package ru.dpanteleev.otus_kotlin.biz.validation

import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.MgRepoStub
import ru.dpanteleev.otus_kotlin.in_memory.MgRepoInMemory
import ru.dpanteleev.otus_kotlin.models.Command

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationUpdateTest {

	private val command = Command.UPDATE
	private val processor = MgProcessor(CoreSettings(repoTest = MgRepoStub()))

	@Test
	fun correctTitle() = validationTitleCorrect(command, processor)
	@Test
	fun trimTitle() = validationTitleTrim(command, processor)
	@Test
	fun emptyTitle() = validationTitleEmpty(command, processor)
	@Test
	fun badSymbolsTitle() = validationTitleSymbols(command, processor)

	@Test
	fun correctDescription() = validationDescriptionCorrect(command, processor)
	@Test
	fun trimDescription() = validationDescriptionTrim(command, processor)
	@Test
	fun emptyDescription() = validationDescriptionEmpty(command, processor)
	@Test
	fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

	@Test
	fun correctId() = validationIdCorrect(command, processor)
	@Test
	fun trimId() = validationIdTrim(command, processor)
	@Test
	fun emptyId() = validationIdEmpty(command, processor)
	@Test
	fun badFormatId() = validationIdFormat(command, processor)

	@Test
	fun correctLock() = validationLockCorrect(command, processor)
	@Test
	fun trimLock() = validationLockTrim(command, processor)
	@Test
	fun emptyLock() = validationLockEmpty(command, processor)
	@Test
	fun badFormatLock() = validationLockFormat(command, processor)


}

