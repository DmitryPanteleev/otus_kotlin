package ru.dpanteleev.otus_kotlin.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.dpanteleev.otus_kotlin.AppSettings
import ru.dpanteleev.otus_kotlin.RabbitProcessorBase
import ru.dpanteleev.otus_kotlin.apiV2RequestDeserialize
import ru.dpanteleev.otus_kotlin.apiV2ResponseSerialize
import ru.dpanteleev.otus_kotlin.config.RabbitConfig
import ru.dpanteleev.otus_kotlin.config.RabbitExchangeConfiguration
import ru.dpanteleev.otus_kotlin.config.coreSettings
import ru.dpanteleev.otus_kotlin.config.rabbitLogger
import ru.dpanteleev.otus_kotlin.fromTransport
import ru.dpanteleev.otus_kotlin.models.IRequest
import ru.dpanteleev.otus_kotlin.process
import ru.dpanteleev.otus_kotlin.toLog
import ru.dpanteleev.otus_kotlin.toTransport

class RabbitDirectProcessorV2(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    settings: AppSettings = coreSettings,
) : RabbitProcessorBase(config, processorConfig) {

    private val logger = settings.logger.logger(RabbitDirectProcessorV1::class)
    private val processor = settings.processor

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(logger, "rabbit-v2",
            {
                apiV2RequestDeserialize<IRequest>(String(message.body)).also {
                    println("TYPE: ${it::class.java.simpleName}")
                    fromTransport(it)
                }
            },
            {
                rabbitLogger.info("start publish")
                val response = toTransport()
                apiV2ResponseSerialize(response).also {
                    println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }.also {
                    println("published")
                }
            },
            { toLog("rabbit-v2" ) })
    }
}

