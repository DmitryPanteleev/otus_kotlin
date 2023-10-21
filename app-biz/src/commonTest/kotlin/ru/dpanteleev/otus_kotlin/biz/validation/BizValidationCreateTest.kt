package ru.dpanteleev.otus_kotlin.biz.validation

import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.in_memory.MgRepoInMemory
import ru.dpanteleev.otus_kotlin.models.Command

// TODO-validation-5: смотрим пример теста валидации, собранного из тестовых функций-оберток
@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

	private val command = Command.CREATE
	private val processor = MgProcessor(CoreSettings(repoTest = MgRepoInMemory()))

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

}

