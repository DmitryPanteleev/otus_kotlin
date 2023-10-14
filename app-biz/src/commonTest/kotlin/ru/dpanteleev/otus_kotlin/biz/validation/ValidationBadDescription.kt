package ru.dpanteleev.otus_kotlin.biz.validation

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.biz.addTestPrincipal
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode

private val stub = Stub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionCorrect(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = stub.id,
			title = "abc",
			description = "abc",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock("123-234-abc-ABC"),
		),
	)
	ctx.addTestPrincipal(stub.bankId)
	processor.exec(ctx)
	assertEquals(0, ctx.errors.size)
	assertNotEquals(State.FAILING, ctx.state)
	assertEquals("abc", ctx.mgValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionTrim(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = stub.id,
			title = "abc",
			description = " \n\tabc \n\t",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock("123-234-abc-ABC"),
		),
	)
	ctx.addTestPrincipal(stub.bankId)
	processor.exec(ctx)
	assertEquals(0, ctx.errors.size)
	assertNotEquals(State.FAILING, ctx.state)
	assertEquals("abc", ctx.mgValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionEmpty(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = stub.id,
			title = "abc",
			description = "",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock("123-234-abc-ABC"),
		),
	)
	ctx.addTestPrincipal(stub.bankId)
	processor.exec(ctx)
	assertEquals(1, ctx.errors.size)
	assertEquals(State.FAILING, ctx.state)
	val error = ctx.errors.firstOrNull()
	assertEquals("description", error?.field)
	assertContains(error?.message ?: "", "description")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionSymbols(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = stub.id,
			title = "abc",
			description = "!@#$%^&*(),.{}",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock("123-234-abc-ABC"),
		),
	)
	ctx.addTestPrincipal(stub.bankId)
	processor.exec(ctx)
	assertEquals(1, ctx.errors.size)
	assertEquals(State.FAILING, ctx.state)
	val error = ctx.errors.firstOrNull()
	assertEquals("description", error?.field)
	assertContains(error?.message ?: "", "description")
}
