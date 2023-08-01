package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import ru.dpanteleev.otus_kotlin.api.v2.models.BorrowerCategory
import ru.dpanteleev.otus_kotlin.api.v2.models.MgCreateObject
import ru.dpanteleev.otus_kotlin.api.v2.models.MgCreateRequest
import ru.dpanteleev.otus_kotlin.api.v2.models.MgCreateResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgDebug
import ru.dpanteleev.otus_kotlin.api.v2.models.MgRequestDebugMode
import ru.dpanteleev.otus_kotlin.api.v2.models.MgRequestDebugStubs
import ru.dpanteleev.otus_kotlin.api.v2.models.MgVisibility
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.RequestId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

class MapperTransportKmpTest {

	@Test
	fun fromTransport() {
		val req = MgCreateRequest(
			requestId = "1234",
			debug = MgDebug(
				mode = MgRequestDebugMode.STUB,
				stub = MgRequestDebugStubs.SUCCESS,
			),
			mg = MgCreateObject(
				title = "title",
				description = "desc",
				borrowerCategory = BorrowerCategory.SALARY,
				visibility = MgVisibility.PUBLIC,
			),
		)

		val context = Context()
		context.fromTransport(req)

		assertEquals(MgStubs.SUCCESS, context.stubCase)
		assertEquals(WorkMode.STUB, context.workMode)
		assertEquals("title", context.mortgageRequest.title)
		assertEquals(Visibility.PUBLIC, context.mortgageRequest.visibility)
		assertEquals(BorrowerCategoryModel.SALARY, context.mortgageRequest.borrowerCategoryModel)
	}

	@Test
	fun toTransport() {
		val context = Context(
			requestId = RequestId("1234"),
			command = Command.CREATE,
			mortgageResponse = mutableListOf(
				Mortgage(
					title = "title",
					description = "desc",
					borrowerCategoryModel = BorrowerCategoryModel.SALARY,
					visibility = Visibility.PUBLIC,
				)
			),
			errors = mutableListOf(
				MgError(
					code = "err",
					group = "request",
					field = "title",
					message = "wrong title",
				)
			),
			state = State.ACTIVE,
		)

		val req = context.toTransport() as MgCreateResponse

		assertEquals("1234", req.requestId)
		assertEquals("title", req.mg?.title)
		assertEquals("desc", req.mg?.description)
		assertEquals(MgVisibility.PUBLIC, req.mg?.visibility)
		assertEquals(BorrowerCategory.SALARY, req.mg?.borrowerCategory)
		assertEquals(1, req.errors?.size)
		assertEquals("err", req.errors?.firstOrNull()?.code)
		assertEquals("request", req.errors?.firstOrNull()?.group)
		assertEquals("title", req.errors?.firstOrNull()?.field)
		assertEquals("wrong title", req.errors?.firstOrNull()?.message)
	}
}