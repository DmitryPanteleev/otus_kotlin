package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.logging.common.LoggerProvider

data class CoreSettings(
    val loggerProvider: LoggerProvider = LoggerProvider()
) {
    companion object {
        val NONE = CoreSettings()
    }
}
