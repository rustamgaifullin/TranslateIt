package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import org.junit.Assert
import org.junit.Test

class BablaUrlTest {

    @Test
    fun should_construct_proper_url() {
        //given
        val sut = BablaUrl()

        //when
        val result = sut.construct("word", LanguageModel("EN", "English"), LanguageModel("PL", "Polish"))

        //then
        Assert.assertEquals("should be the same", "https://en.bab.la/dictionary/english-polish/word", result)
    }
}