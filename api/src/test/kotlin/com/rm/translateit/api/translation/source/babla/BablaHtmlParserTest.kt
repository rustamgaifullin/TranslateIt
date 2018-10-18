package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.translation.Words.Companion.words
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class BablaHtmlParserTest {

    @Test
    fun should_return_items_from_html_string() {
        //given
        val sut = BablaHtmlParser()

        //when
        val result = sut.getTranslateItemsFrom(successfulResponseWithTranslation())

        //then
        assertTrue("Result list should not be empty", result.toOneLineString().isNotEmpty())
        assertEquals("Result list list should be the same as expected", expectedResult(), result)
    }

    @Test
    fun should_return_empty_list_from_result_without_translation() {
        //given
        val sut = BablaHtmlParser()

        //when
        val result = sut.getTranslateItemsFrom(successfulResponseWithoutTranslation())

        //then
        assertTrue("Result list should be empty", result.toOneLineString().isEmpty())
    }

    private fun expectedResult() = words("pozdrowienie", "pozdrowienia", "witam", "dzień dobry", "cześć")

    private fun successfulResponseWithTranslation(): String {
        val responsePath = getResponsePath(forFile = "babla_response_with_translation.html")

        return File(responsePath).readText()
    }

    private fun successfulResponseWithoutTranslation(): String {
        val responsePath = getResponsePath(forFile = "babla_response_without_translation.html")

        return File(responsePath).readText()
    }

    private fun getResponsePath(forFile: String) = BablaHtmlParserTest::class.java.classLoader.getResource(forFile).path
}