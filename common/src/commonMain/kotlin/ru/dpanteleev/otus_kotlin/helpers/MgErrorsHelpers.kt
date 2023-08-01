package ru.dpanteleev.otus_kotlin.helpers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.MgError

fun Throwable.asMgError(
	code: String = "unknown",
	group: String = "unknown",
	message: String = this.message ?: ""
) = MgError(
	code = code,
	group = group,
	field = "",
	message = message,
	exception = this
)

fun Context.addError(vararg error: MgError) = errors.addAll(error)