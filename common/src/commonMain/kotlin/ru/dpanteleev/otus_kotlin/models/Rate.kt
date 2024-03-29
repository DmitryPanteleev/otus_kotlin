package ru.dpanteleev.otus_kotlin.models

import kotlin.jvm.JvmInline
import kotlin.math.withSign

@JvmInline
value class Rate(private val rate: Double) {
	fun asString() = rate.withSign(2).toString()

	fun asLong() = rate

	companion object {
		val NONE = Rate(Double.NaN)
	}
}