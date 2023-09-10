package ru.dpanteleev.otus_kotlin.helpers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.State

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

fun Context.fail(error: MgError) {
	addError(error)
	state = State.FAILING
}

fun errorValidation(
	field: String,
	/**
	 * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
	 * Например: empty, badSymbols, tooLong, etc
	 */
	violationCode: String,
	description: String,
	level: MgError.Level = MgError.Level.ERROR,
) = MgError(
	code = "validation-$field-$violationCode",
	field = field,
	group = "validation",
	message = "Validation error for field $field: $description",
	level = level,
)
