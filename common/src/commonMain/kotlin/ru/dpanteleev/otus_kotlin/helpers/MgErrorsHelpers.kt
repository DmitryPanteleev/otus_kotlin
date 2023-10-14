package ru.dpanteleev.otus_kotlin.helpers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.exceptions.RepoConcurrencyException
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.MgLock
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


fun errorAdministration(
	/**
	 * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
	 * Например: empty, badSymbols, tooLong, etc
	 */
	field: String = "",
	violationCode: String,
	description: String,
	exception: Exception? = null,
	level: MgError.Level = MgError.Level.ERROR,
) = MgError(
	field = field,
	code = "administration-$violationCode",
	group = "administration",
	message = "Microservice management error: $description",
	level = level,
	exception = exception,
)

fun errorRepoConcurrency(
	expectedLock: MgLock,
	actualLock: MgLock?,
	exception: Exception? = null,
) = MgError(
	field = "lock",
	code = "concurrency",
	group = "repo",
	message = "The object has been changed concurrently by another user or process",
	exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val errorNotFound = MgError(
	field = "id",
	message = "Not Found",
	code = "not-found"
)

val errorEmptyId = MgError(
	field = "id",
	message = "Id must not be null or blank"
)