package ru.dpanteleev.otus_kotlin
import apiV2Mapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.dpanteleev.otus_kotlin.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response: IResponse = MgCreateResponse(
        responseType = "create",
        requestId = "123",
        mg = MgResponseObject(
            title = "mg title",
            description = "mg description",
            borrowerCategory = BorrowerCategory.SALARY,
            visibility = MgVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(response)

        assertContains(json, Regex("\"title\":\\s*\"mg title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString(json) as MgCreateResponse

        assertEquals(response, obj)
    }
}