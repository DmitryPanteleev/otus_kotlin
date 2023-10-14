package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.helpers.errorEmptyId as _errorEmptyId
import ru.dpanteleev.otus_kotlin.helpers.errorNotFound as _errorNotFound
import ru.dpanteleev.otus_kotlin.helpers.errorRepoConcurrency
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage

data class DbMgResponse(
	override val data: Mortgage?,
	override val isSuccess: Boolean,
	override val errors: List<MgError> = emptyList()
) : IDbResponse<Mortgage> {

	companion object {
		val MOCK_SUCCESS_EMPTY = DbMgResponse(null, true)
		fun success(result: Mortgage) = DbMgResponse(result, true)
		fun error(errors: List<MgError>, data: Mortgage? = null) = DbMgResponse(data, false, errors)
		fun error(error: MgError, data: Mortgage? = null) = DbMgResponse(data, false, listOf(error))

		val errorEmptyId = error(_errorEmptyId)

		fun errorConcurrent(lock: MgLock, ad: Mortgage?) = error(
			errorRepoConcurrency(lock, ad?.lock?.let { MgLock(it.asString()) }),
			ad
		)

		val errorNotFound = error(_errorNotFound)
	}
}
