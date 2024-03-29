package ru.dpanteleev.otus_kotlin.config

// TODO-rmq-3: наш класс настроек взаимодействия с RMQ
data class RabbitExchangeConfiguration(
    val keyIn: String,
    val keyOut: String,
    val exchange: String,
    val queueIn: String,
    val queueOut: String,
    val consumerTag: String,
    val exchangeType: String = "direct" // Объявляем обменник типа "type" (сообщения передаются в те очереди, где ключ совпадает)
)
