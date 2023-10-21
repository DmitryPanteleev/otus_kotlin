package ru.dpanteleev.otus_kotlin.models

import kotlin.jvm.JvmInline

@JvmInline
value class MortgageId(private val id: String) {
	fun asString() = id

	override fun toString(): String {
		return id
	}


	companion object {
		val NONE = MortgageId("0")
	}
}