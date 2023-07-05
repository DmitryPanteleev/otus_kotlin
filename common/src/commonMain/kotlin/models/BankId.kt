package models

import kotlin.jvm.JvmInline

@JvmInline
value class BankId(
	private val id: Long
) {
	fun asString() = id.toString()

	companion object{
		val NONE = BankId(-1)
	}
}