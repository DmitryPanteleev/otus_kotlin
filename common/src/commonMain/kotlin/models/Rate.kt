package models

import kotlin.jvm.JvmInline
import kotlin.math.withSign

@JvmInline
value class Rate(private val rate: Double) {
	fun asString() = rate.withSign(2).toString()

	companion object {
		val NONE = Rate(Double.NaN)
	}
}