package com.rm.translateit.api.languages

import org.junit.Test
import kotlin.test.assertTrue

class StaticLanguagesTest {
    @Test
    fun languagesTest() {
        //given
        val sut = StaticLanguages()

        //when
        val result = sut.languages()

        //then
        assertTrue(result.isNotEmpty(), "List of languages should not be empty")
    }
}