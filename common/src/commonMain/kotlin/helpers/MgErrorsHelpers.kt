package helpers

import Context
import models.MgError

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