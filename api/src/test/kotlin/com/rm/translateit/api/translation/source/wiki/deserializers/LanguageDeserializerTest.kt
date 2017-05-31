package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.junit.Test
import kotlin.test.assertEquals

class LanguageDeserializerTest {
    @Test
    fun deserialize_json_with_correct_data() {
        //given
        val sut = LanguageDeserializer()

        //when
        val result = sut.deserialize(jsonWithData(), null, null)

        //then
        assertEquals(result.code, "pl", "first element should contain language code")
        assertEquals(result.title, "translation", "first element should contain translation result")
    }

    //TODO: DSL
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