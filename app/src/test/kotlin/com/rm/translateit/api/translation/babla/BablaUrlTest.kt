package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language
import org.junit.Assert
import org.junit.Test

class BablaUrlTest {

    @Test
    fun should_construct_proper_url() {
        //given
        val sut = BablaUrl()

        //when
        val result = sut.construct("word", Language("EN", "English"), Language("PL", "Polish"))

        //then
        Assert.assertEquals("should be the same", "http://en.bab.la/dictionary/english-polish/word", result)
    }
}