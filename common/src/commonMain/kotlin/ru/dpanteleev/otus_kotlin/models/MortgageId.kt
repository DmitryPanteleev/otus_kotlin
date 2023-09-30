package ru.dpanteleev.otus_kotlin.models

import kotlin.jvm.JvmInline

@JvmInline
value class MortgageId(private val id: Long) {
	fun asString() = id.toString()

	fun toLong() = id

	companion object {
		val NONE = MortgageId(-1)
	}
}