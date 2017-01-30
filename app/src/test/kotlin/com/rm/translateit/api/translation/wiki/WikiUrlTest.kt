package com.rm.translateit.api.translation.wiki

import com.rm.translateit.api.models.Language
import org.junit.Assert
import org.junit.Test

class WikiUrlTest {

    @Test
    fun should_construct_proper_url() {
        //given
        val sut = WikiUrl()

        //when
        val result = sut.construct("word", Language("EN", "English"), Language("PL", "Polish"))

        //then
        Assert.assertEquals("should be the same", "https://en.wikipedia.org/w/api.php?action=query&prop=langlinks&format=json&lllimit=500&redirects&lllang=pl&titles=word", result)
    }
}