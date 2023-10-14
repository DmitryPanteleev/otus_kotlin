package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.models.MgError

interface IDbResponse<T> {
	val data: T?
	val isSuccess: Boolean
	val errors: List<MgError>
}
