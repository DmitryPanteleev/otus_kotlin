import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.dpanteleev.otus_kotlin.api.v1.models.IRequest
import ru.dpanteleev.otus_kotlin.api.v1.models.IResponse

val apiV1Mapper = JsonMapper.builder().run {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T = apiV1Mapper.readValue(json, IRequest::class.java) as T

fun apiV1ResponseSerializer(response: IRequest): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV1ResponseSerializer(json: String): T = apiV1Mapper.readValue(json, IResponse::class.java) as T