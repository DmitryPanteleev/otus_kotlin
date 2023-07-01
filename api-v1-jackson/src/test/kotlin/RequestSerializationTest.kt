import ru.dpanteleev.otus_kotlin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = MgCreateRequest(
        requestId = "123",
        debug = MgDebug(
            mode = MgRequestDebugMode.STUB,
            stub = MgRequestDebugStubs.BAD_TITLE
        ),
        mg = MgCreateObject(
            title = "ad title",
            description = "ad description",
            mgType = DealSide.DEMAND,
            visibility = MgVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MgCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, MgCreateRequest::class.java)

        assertEquals("123", obj.requestId)
    }
}