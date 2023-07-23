package ru.dpanteleev.otus_kotlin
import apiV2Mapper
import apiV2RequestDeserialize
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.dpanteleev.otus_kotlin.api.v2.models.*

class RequestSerializationTest {
    private val request: IRequest = MgCreateRequest(
        requestType = "create",
        requestId = "123",
        debug = MgDebug(
            mode = MgRequestDebugMode.STUB,
            stub = MgRequestDebugStubs.BAD_TITLE
        ),
        mg = MgCreateObject(
            title = "mg title",
            description = "mg description",
            borrowerCategory = BorrowerCategory.SALARY,
            visibility = MgVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"mg title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString(json) as MgCreateRequest

        assertEquals(request, obj)
    }
    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
            "requestType":"create",
            "requestId":"123",
            "debug":{"mode":"stub","stub":"badTitle"},
            "mg":{"title":"mg title","description":"mg description","borrowerCategory":"salary","visibility":"public","productId":null}
            }
        """.trimIndent()
        val obj = apiV2RequestDeserialize(jsonString) as IRequest

        assertEquals("123", obj.requestId)
        assertEquals(request, obj)
    }
}