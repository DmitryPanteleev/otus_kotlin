package ru.dpanteleev.otus_kotlin

import kotlinx.datetime.Instant

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)

val Instant.Companion.NONE: Instant
	get() = INSTANT_NONE