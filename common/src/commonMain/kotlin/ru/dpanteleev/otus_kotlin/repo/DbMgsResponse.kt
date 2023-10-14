package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.Mortgage

data class DbMgsResponse(
	override val data: List<Mortgage>?,
	override val isSuccess: Boolean,
	override val errors: List<MgError> = emptyList(),
) : IDbResponse<List<Mortgage>> {

	companion object {
		val MOCK_SUCCESS_EMPTY = DbMgsResponse(emptyList(), true)
		fun success(result: List<Mortgage>) = DbMgsResponse(result, true)
		fun error(errors: List<MgError>) = DbMgsResponse(null, false, errors)
		fun error(error: MgError) = DbMgsResponse(null, false, listOf(error))
	}
}
