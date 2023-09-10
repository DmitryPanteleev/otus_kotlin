package ru.dpanteleev.otus_kotlin.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.dpanteleev.otus_kotlin.config.RabbitConfig
import ru.dpanteleev.otus_kotlin.config.RabbitExchangeConfiguration
import ru.dpanteleev.otus_kotlin.config.corSettings

class RabbitDirectProcessorV2(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    settings: MkplAppSettings = corSettings,
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
                val response = toTransportAd()
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

