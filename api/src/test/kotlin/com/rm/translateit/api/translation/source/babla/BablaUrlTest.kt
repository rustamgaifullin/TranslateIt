package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.NameModel
import org.junit.Assert.assertEquals
import org.junit.Test

class BablaUrlTest {

    @Test
    fun should_construct_proper_url() {
        //given
        val sut = BablaUrl()

        //when
        val names = listOf(
                NameModel("en", "english"),
                NameModel("pl", "polish")
        )
        val result = sut.construct("word", LanguageModel("EN", names), LanguageModel("PL", names))

        //then
        assertEquals("https://en.bab.la/dictionary/english-polish/word", result)
    }

    @Test
    fun should_construct_proper_url_with_spaces() {
        //given
        val sut = BablaUrl()

        //when
        val names = listOf(
                NameModel("en", "english"),
                NameModel("pl", "polish")
        )
        val result = sut.construct(
                "word with spaces",
                LanguageModel("EN", names),
                LanguageModel("PL", names))

        //then
        assertEquals("https://en.bab.la/dictionary/english-polish/word-with-spaces", result)
    }

    @Test
    fun should_construct_proper_url_for_russian_language() {
        //given
        val sut = BablaUrl()

        //when
        val names = listOf(
                NameModel("ru", "russian"),
                NameModel("pl", "polish")
        )
        val result = sut.construct("word", LanguageModel("RU", names), LanguageModel("PL", names))

        //then
        assertEquals("https://www.babla.ru/russian-polish/word", result)
    }

    @Test
    fun should_construct_proper_url_with_spaces_for_russian_language() {
        //given
        val sut = BablaUrl()

        //when
        val names = listOf(
                NameModel("ru", "russian"),
                NameModel("pl", "polish")
        )
        val result = sut.construct(
                "word with spaces",
                LanguageModel("RU", names),
                LanguageModel("PL", names))

        //then
        assertEquals("https://www.babla.ru/russian-polish/word-with-spaces", result)
    }
}