package ru.dpanteleev.otus_kotlin.controller

import RabbitProcessorBase
import kotlinx.coroutines.*
import ru.dpanteleev.otus_kotlin.config.rabbitLogger

// TODO-rmq-5: запуск процессора
class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {

    fun start() = runBlocking {
        rabbitLogger.info("start init processors")
        processors.forEach {
            try {
                    launch { it.process() }
            } catch (e: RuntimeException) {
                // логируем, что-то делаем
                e.printStackTrace()
            }

        }
    }

}
