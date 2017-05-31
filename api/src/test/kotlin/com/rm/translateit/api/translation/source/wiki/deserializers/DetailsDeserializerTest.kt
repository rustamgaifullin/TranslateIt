package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.junit.Test
import kotlin.test.assertEquals

class DetailsDeserializerTest {
    @Test
    fun deserialize_json_with_correct_data() {
        //given
        val sut = DetailsDeserializer()

        //when
        val result = sut.deserialize(jsonWithData(), null, null)

        //then
        assertEquals(result.description, "Full description")
        assertEquals(result.url, "www.url.com")
    }

    //TODO: DSL
    private fun jsonWithData(): JsonElement? {
        val rootObject = JsonObject()
        val queryObject = JsonObject()
        val pagesObject = JsonObject()
        val idObject = JsonObject()

        rootObject.add("query", queryObject)
        queryObject.add("pages", pagesObject)
        pagesObject.add("1234",idObject)

        idObject.addProperty("extract", "Full description")
        idObject.addProperty("fullurl", "www.url.com")

        return rootObject
    }
}