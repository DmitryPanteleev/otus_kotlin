package models

import kotlin.jvm.JvmInline

@JvmInline
value class MortgageId(private val id: Long) {
	fun asString() = id.toString()

	companion object {
		val NONE = MortgageId(-1)
	}
}