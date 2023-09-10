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
import ru.dpanteleev.otus_kotlin.fromTransport
import ru.dpanteleev.otus_kotlin.models.IRequest
import ru.dpanteleev.otus_kotlin.process
import ru.dpanteleev.otus_kotlin.toLog
import ru.dpanteleev.otus_kotlin.toTransport


class RabbitDirectProcessorV1(
	config: RabbitConfig,
	processorConfig: RabbitExchangeConfiguration,
	settings: AppSettings = coreSettings,
) : RabbitProcessorBase(config, processorConfig) {

	private val logger = settings.logger.logger(RabbitDirectProcessorV1::class)
	private val processor = settings.processor

	override suspend fun Channel.processMessage(message: Delivery) {
		processor.process(logger, "rabbit-v1",
			{
				(apiV2RequestDeserialize(message.body.toString()) as IRequest).run {
					fromTransport(this).also {
						println("TYPE: ${this::class.simpleName}")
					}
				}
			},
			{
				val response = toTransport()
				apiV2ResponseSerialize(response).encodeToByteArray().also {
					println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
					basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
				}.also {
					println("published")
				}
			},
			{ toLog("rabbit") })
	}
}