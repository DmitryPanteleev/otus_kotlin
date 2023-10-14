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
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode

private val stub = Stub.prepareResult { id = MortgageId("123-234-abc-ABC") }

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockCorrect(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = MortgageId("123-234-abc-ABC"),
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
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockTrim(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = MortgageId("123-234-abc-ABC"),
			title = "abc",
			description = "abc",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock(" \n\t 123-234-abc-ABC \n\t "),
		),
	)
	ctx.addTestPrincipal(stub.bankId)
	processor.exec(ctx)
	assertEquals(0, ctx.errors.size)
	assertNotEquals(State.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockEmpty(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = MortgageId("123-234-abc-ABC"),
			title = "abc",
			description = "abc",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock(""),
		),
	)
	processor.exec(ctx)
	assertEquals(1, ctx.errors.size)
	assertEquals(State.FAILING, ctx.state)
	val error = ctx.errors.firstOrNull()
	assertEquals("lock", error?.field)
	assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockFormat(command: Command, processor: MgProcessor) = runTest {
	val ctx = Context(
		command = command,
		state = State.NONE,
		workMode = WorkMode.TEST,
		mortgageRequest = Mortgage(
			id = MortgageId("123-234-abc-ABC"),
			title = "abc",
			description = "abc",
			borrowerCategoryModel = BorrowerCategoryModel.SALARY,
			visibility = Visibility.PUBLIC,
			lock = MgLock("!@#\$%^&*(),.{}"),
		),
	)
	processor.exec(ctx)
	assertEquals(1, ctx.errors.size)
	assertEquals(State.FAILING, ctx.state)
	val error = ctx.errors.firstOrNull()
	assertEquals("lock", error?.field)
	assertContains(error?.message ?: "", "id")
}
