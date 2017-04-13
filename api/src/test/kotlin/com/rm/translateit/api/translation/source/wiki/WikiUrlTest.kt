package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.models.LanguageModel
import org.junit.Assert
import org.junit.Test

class WikiUrlTest {

    @Test
    fun should_construct_proper_url() {
        //given
        val sut = WikiUrl()

        //when
        val result = sut.construct("word", LanguageModel("EN", "English"), LanguageModel("PL", "Polish"))

        //then
        Assert.assertEquals("should be the same", "https://en.wikipedia.org/w/api.php?action=query&prop=langlinks&format=json&lllimit=500&redirects&lllang=pl&titles=word", result)
    }
}