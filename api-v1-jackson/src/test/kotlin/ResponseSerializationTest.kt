import ru.dpanteleev.otus_kotlin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = MgCreateResponse(
        requestId = "123",
        mg = MgResponseObject(
            title = "ad title",
            description = "ad description",
            mgType = DealSide.DEMAND,
            visibility = MgVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MgCreateResponse

        assertEquals(response, obj)
    }
}