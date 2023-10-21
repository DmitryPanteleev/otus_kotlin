package ru.dpanteleev.otus_kotlin.logging.jvm

import ch.qos.logback.classic.Logger
import kotlin.reflect.KClass
import org.slf4j.LoggerFactory
import ru.dpanteleev.otus_kotlin.logging.common.IMpLogWrapper

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mgLoggerLogback(logger: Logger): IMpLogWrapper = MgLogWrapperLogback(
	logger = logger,
	loggerId = logger.name,
)

fun mgLoggerLogback(clazz: KClass<*>): IMpLogWrapper = mgLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun mgLoggerLogback(loggerId: String): IMpLogWrapper = mgLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
