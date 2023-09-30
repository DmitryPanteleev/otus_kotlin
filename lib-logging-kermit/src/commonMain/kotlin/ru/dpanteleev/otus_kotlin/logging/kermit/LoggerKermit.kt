package ru.dpanteleev.otus_kotlin.logging.kermit

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import kotlin.reflect.KClass
import ru.dpanteleev.otus_kotlin.logging.common.IMpLogWrapper

@Suppress("unused")
fun loggerKermit(loggerId: String): IMpLogWrapper {
	val logger = Logger(
		config = StaticConfig(
			minSeverity = Severity.Info,
			logWriterList = listOf(CommonWriter(), platformLogWriter())
		),
		tag = "DEV"
	)
	return LoggerWrapperKermit(
		logger = logger,
		loggerId = loggerId,
	)
}

@Suppress("unused")
fun loggerKermit(cls: KClass<*>): IMpLogWrapper {
	val logger = Logger(
		config = StaticConfig(
			minSeverity = Severity.Info,
			logWriterList = listOf(CommonWriter(), platformLogWriter())
		),
		tag = "DEV"
	)
	return LoggerWrapperKermit(
		logger = logger,
		loggerId = cls.qualifiedName ?: "",
	)
}
