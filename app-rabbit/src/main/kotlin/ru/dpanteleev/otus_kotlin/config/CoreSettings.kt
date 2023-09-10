package ru.dpanteleev.otus_kotlin.config

import ru.dpanteleev.otus_kotlin.AppSettings
import ru.dpanteleev.otus_kotlin.logging.common.IMpLogWrapper
import ru.dpanteleev.otus_kotlin.logging.common.LoggerProvider

private val loggerProvider = LoggerProvider { IMpLogWrapper.DEFAULT }

val coreSettings = AppSettings(
    logger = loggerProvider
)