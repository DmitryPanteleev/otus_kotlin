package ru.dpanteleev.otus_kotlin.api

import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import kotlinx.serialization.encodeToString
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.apiV2Mapper
import ru.dpanteleev.otus_kotlin.fromTransport
import ru.dpanteleev.otus_kotlin.models.MgCreateRequest
import ru.dpanteleev.otus_kotlin.models.MgDeleteRequest
import ru.dpanteleev.otus_kotlin.models.MgReadRequest
import ru.dpanteleev.otus_kotlin.models.MgSearchRequest
import ru.dpanteleev.otus_kotlin.models.MgUpdateRequest
import ru.dpanteleev.otus_kotlin.toTransportCreate
import ru.dpanteleev.otus_kotlin.toTransportDelete
import ru.dpanteleev.otus_kotlin.toTransportRead
import ru.dpanteleev.otus_kotlin.toTransportSearch
import ru.dpanteleev.otus_kotlin.toTransportUpdate

suspend fun ApplicationCall.create(processor: MgProcessor) {
	val request = apiV2Mapper.decodeFromString<MgCreateRequest>(receiveText())
	val context = Context()
	context.fromTransport(request)
	processor.exec(context)
	respondText(apiV2Mapper.encodeToString(context.toTransportCreate()), ContentType.Application.Json)
}

suspend fun ApplicationCall.read(processor: MgProcessor) {
	val request = apiV2Mapper.decodeFromString<MgReadRequest>(receiveText())
	val context = Context()
	context.fromTransport(request)
	processor.exec(context)
	respondText(apiV2Mapper.encodeToString(context.toTransportRead()), ContentType.Application.Json)
}

suspend fun ApplicationCall.update(processor: MgProcessor) {
	val request = apiV2Mapper.decodeFromString<MgUpdateRequest>(receiveText())
	val context = Context()
	context.fromTransport(request)
	processor.exec(context)
	respondText(apiV2Mapper.encodeToString(context.toTransportUpdate()), ContentType.Application.Json)
}

suspend fun ApplicationCall.delete(processor: MgProcessor) {
	val request = apiV2Mapper.decodeFromString<MgDeleteRequest>(receiveText())
	val context = Context()
	context.fromTransport(request)
	processor.exec(context)
	respondText(apiV2Mapper.encodeToString(context.toTransportDelete()), ContentType.Application.Json)
}

suspend fun ApplicationCall.search(processor: MgProcessor) {
	val request = apiV2Mapper.decodeFromString<MgSearchRequest>(receiveText())
	val context = Context()
	context.fromTransport(request)
	processor.exec(context)
	respondText(apiV2Mapper.encodeToString(context.toTransportSearch()), ContentType.Application.Json)
}
