package ru.dpanteleev.otus_kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.RequestId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility

class MapperTest {

	@Test
	fun fromContext() {
		val context = Context(
			requestId = RequestId("1234"),
			command = Command.CREATE,
			mortgageResponse = mutableListOf(
				Mortgage(
					MortgageId(666),
					title = "title",
					description = "desc",
					borrowerCategoryModel = BorrowerCategoryModel.EMPLOYEE,
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

		val log = context.toLog("test-id")

		assertEquals("test-id", log.logId)
		assertEquals("mortgage_place", log.source)
		assertEquals("1234", log.mg?.requestId)
		assertEquals("PUBLIC", log.mg?.responseAds?.firstOrNull()?.visibility)
		val error = log.errors?.firstOrNull()
		assertEquals("wrong title", error?.message)
		assertEquals("ERROR", error?.level)
	}
}
