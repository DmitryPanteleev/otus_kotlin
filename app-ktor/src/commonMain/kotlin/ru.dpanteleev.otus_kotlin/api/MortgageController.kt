package ru.dpanteleev.otus_kotlin.api

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import apiV2Mapper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.dpanteleev.otus_kotlin.fromTransport
import ru.dpanteleev.otus_kotlin.toTransportCreate
import ru.dpanteleev.otus_kotlin.toTransportDelete
import ru.dpanteleev.otus_kotlin.toTransportRead
import ru.dpanteleev.otus_kotlin.toTransportSearch
import ru.dpanteleev.otus_kotlin.toTransportUpdate
import ru.dpanteleev.otus_kotlin.api.v2.models.MgCreateRequest
import ru.dpanteleev.otus_kotlin.api.v2.models.MgDeleteRequest
import ru.dpanteleev.otus_kotlin.api.v2.models.MgReadRequest
import ru.dpanteleev.otus_kotlin.api.v2.models.MgSearchRequest
import ru.dpanteleev.otus_kotlin.api.v2.models.MgUpdateRequest

suspend fun ApplicationCall.create(processor: MgProcessor) {
    val request = apiV2Mapper.decodeFromString<MgCreateRequest>(receiveText())
    val context = Context()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportCreate()))
}

suspend fun ApplicationCall.read(processor: MgProcessor) {
    val request = apiV2Mapper.decodeFromString<MgReadRequest>(receiveText())
    val context = Context()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportRead()))
}

suspend fun ApplicationCall.update(processor: MgProcessor) {
    val request = apiV2Mapper.decodeFromString<MgUpdateRequest>(receiveText())
    val context = Context()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportUpdate()))
}

suspend fun ApplicationCall.delete(processor: MgProcessor) {
    val request = apiV2Mapper.decodeFromString<MgDeleteRequest>(receiveText())
    val context = Context()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportDelete()))
}

suspend fun ApplicationCall.search(processor: MgProcessor) {
    val request = apiV2Mapper.decodeFromString<MgSearchRequest>(receiveText())
    val context = Context()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportSearch()))
}
