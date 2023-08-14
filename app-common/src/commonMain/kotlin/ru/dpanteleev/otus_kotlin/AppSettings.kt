package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.logging.common.LoggerProvider


data class AppSettings(
    val appUrls: List<String> = emptyList(),
    val processor: MgProcessor = MgProcessor(),
    val logger: LoggerProvider = LoggerProvider()
)
