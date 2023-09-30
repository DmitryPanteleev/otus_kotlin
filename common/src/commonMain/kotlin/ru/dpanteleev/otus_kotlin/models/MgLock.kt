package ru.dpanteleev.otus_kotlin.models

import kotlin.jvm.JvmInline

@JvmInline
value class MgLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MgLock("")
    }
}
