package models

data class MgError(
	val code: String = "",
	val group: String = "",
	val field: String = "",
	val message: String = "",
	val exception: Throwable? = null
)
