package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.GsonBuilder
import com.rm.translateit.api.translation.source.wiki.WikiSourceTest
import com.rm.translateit.api.translation.source.wiki.response.DetailsResponse
import com.rm.translateit.api.translation.source.wiki.response.LanguageResponse
import org.junit.Test
import java.io.InputStreamReader
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LanguageTypeAdapterFactoryTest {
    val gson = GsonBuilder()
            .registerTypeAdapterFactory(LanguageTypeAdapterFactory())
            .create()

    @Test
    fun should_be_able_to_create_language_response() {
        val languageResponse = gson.fromJson(getResponseReader("wiki_translation_response.json"), LanguageResponse::class.java)

        assertEquals("pl", languageResponse.code)
        assertEquals("Translate", languageResponse.title)
    }

    @Test
    fun should_be_able_to_create_details_response() {
        val detailsResponse = gson.fromJson(getResponseReader("wiki_translation_details_response.json"), DetailsResponse::class.java)

        assertEquals("Full translation description", detailsResponse.description)
        assertEquals("https://en.wikipedia.org/wiki/Translate", detailsResponse.url)
    }

    @Test
    fun should_be_able_return_empty_language_response() {
        val languageResponse = gson.fromJson(getResponseReader("wiki_translation_empty_response.json"), LanguageResponse::class.java)

        assertTrue(languageResponse.title.isEmpty())
        assertTrue(languageResponse.code.isEmpty())
    }

    @Test
    fun should_be_able_return_empty_details_response() {
        val detailsResponse = gson.fromJson(getResponseReader("wiki_translation_empty_response.json"), DetailsResponse::class.java)

        assertTrue(detailsResponse.description.isEmpty())
        assertTrue(detailsResponse.url.isEmpty())
    }

    private fun getResponseReader(forFile: String) = InputStreamReader(
            WikiSourceTest::class.java.classLoader.getResourceAsStream(forFile)
    )
}