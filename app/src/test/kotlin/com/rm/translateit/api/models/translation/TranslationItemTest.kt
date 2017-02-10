package com.rm.translateit.api.models.translation

import com.rm.translateit.api.models.translation.Tags.Companion.tags
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.ui.decarators.TranslationResultDecorator
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class TranslationItemTest {
    @Test
    fun should_return_one_line_string_based_on_decorator() {
        //given
        val sut = TranslationItem(words("first, second"), tags("tag1"))
        val decorator = Mockito.mock(TranslationResultDecorator::class.java)
        val decoratorResult = "first, second - tag1"

        //when
        `when`(decorator.toSingleLine(sut)).thenReturn(decoratorResult)
        val result = sut.toOneLine(decorator)

        //then
        Assert.assertEquals("result should be equal", decoratorResult, result)
    }
}