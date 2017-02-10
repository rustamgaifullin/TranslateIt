package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.translation.Tags.Companion.tags
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import org.junit.Assert
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
        Assert.assertTrue("Result list should not be empty", result.isNotEmpty())
        Assert.assertEquals("Result list list should be the same as expected", expectedResult(), result)
    }

    @Test
    fun should_return_empty_list_from_result_without_translation() {
        //given
        val sut = BablaHtmlParser()

        //when
        val result = sut.getTranslateItemsFrom(successfulResponseWithoutTranslation())

        //then
        Assert.assertTrue("Result list should be empty", result.isEmpty())
    }

    private fun expectedResult() = listOf(
            TranslationItem(words("pozdrowienie", "pozdrowienia"), tags("[приве́т]", "{m}")),
            TranslationItem(words("witam", "dzień dobry", "cześć"), tags("[приве́т]", "{interj.}"))
    )

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