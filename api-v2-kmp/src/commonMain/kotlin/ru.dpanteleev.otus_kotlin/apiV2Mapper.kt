package ru.dpanteleev.otus_kotlin

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import ru.dpanteleev.otus_kotlin.api.v2.models.*

/**
 * Добавляйте сюда элементы при появлении новых наследников IRequest / IResponse
 */
internal val infos = listOf(
	info(MgCreateRequest::class, IRequest::class, "create") { copy(requestType = it) },
	info(MgReadRequest::class, IRequest::class, "read") { copy(requestType = it) },
	info(MgUpdateRequest::class, IRequest::class, "update") { copy(requestType = it) },
	info(MgDeleteRequest::class, IRequest::class, "delete") { copy(requestType = it) },
	info(MgSearchRequest::class, IRequest::class, "search") { copy(requestType = it) },
	info(MgOffersRequest::class, IRequest::class, "offers") { copy(requestType = it) },

	info(MgCreateResponse::class, IResponse::class, "create") { copy(responseType = it) },
	info(MgReadResponse::class, IResponse::class, "read") { copy(responseType = it) },
	info(MgUpdateResponse::class, IResponse::class, "update") { copy(responseType = it) },
	info(MgDeleteResponse::class, IResponse::class, "delete") { copy(responseType = it) },
	info(MgSearchResponse::class, IResponse::class, "search") { copy(responseType = it) },
	info(MgOffersResponse::class, IResponse::class, "offers") { copy(responseType = it) },
	info(MgInitResponse::class, IResponse::class, "init") { copy(responseType = it) },
)

val apiV2Mapper = Json {
	classDiscriminator = "_"
	encodeDefaults = true
	ignoreUnknownKeys = true

	serializersModule = SerializersModule {
		setupPolymorphic()
	}
}

fun apiV2RequestSerialize(request: IRequest): String = apiV2Mapper.encodeToString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String): T =
	apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(response: IResponse): String = apiV2Mapper.encodeToString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String): T =
	apiV2Mapper.decodeFromString<IResponse>(json) as T