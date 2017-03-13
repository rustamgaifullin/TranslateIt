package com.rm.translateit.api.translation.source.wiki

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.rm.translateit.api.translation.source.wiki.deserializers.LanguageDeserializer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LanguageDeserializerTest {
    @Test
    fun deserialize_json_with_correct_data() {
        //given
        val sut = LanguageDeserializer()

        //when
        val result = sut.deserialize(jsonWithData(), null, null)

        //then
        assertEquals(result.list.size, 1, "list should not be empty")
        assertEquals(result.list[0].code, "pl", "first element should contain language code")
        assertEquals(result.list[0].title, "translation", "first element should contain translation result")
    }

    @Test
    fun deserialize_json_with_empty_data() {
        //given
        val sut = LanguageDeserializer()

        //when
        val result = sut.deserialize(JsonObject(), null, null)

        //then
        assertNotNull(result, "Should not be a null")
        assertNotNull(result.list, "Should not be a null")
        assertEquals(result.list.size, 0, "List should be empty")
    }

    @Test
    fun deserialize_json_with_null_data() {
        //given
        val sut = LanguageDeserializer()

        //when
        val result = sut.deserialize(null, null, null)

        //then
        assertNotNull(result, "Should not be a null")
        assertNotNull(result.list, "Should not be a null")
        assertEquals(result.list.size, 0, "List should be empty")
    }

    private fun jsonWithData(): JsonElement? {
        val rootObject = JsonObject()
        val queryObject = JsonObject()
        val pagesObject = JsonObject()
        val idObject = JsonObject()
        val langLinksArray = JsonArray()
        val langLinkProperty = JsonObject()
        langLinkProperty.addProperty("lang", "pl")
        langLinkProperty.addProperty("*", "translation")

        rootObject.add("query", queryObject)
        queryObject.add("pages", pagesObject)
        pagesObject.add("1234",idObject)
        idObject.add("langlinks", langLinksArray)
        langLinksArray.add(langLinkProperty)

        return rootObject
    }
}