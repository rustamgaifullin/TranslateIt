package com.rm.translateit.api.models.translation

import com.rm.translateit.api.models.translation.Words.Companion.words
import org.junit.Assert
import org.junit.Test

class WordsTest {
  @Test
  fun should_return_string_from_one_item_in_word_list() {
    //given
    val sut = words("One word")

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals("the string should be equal to one word", "One word", result)
  }

  @Test
  fun should_return_string_from_few_items_in_word_list() {
    //given
    val sut = words("First word", "Second word")

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals(
        "the string should be formed from several words", "First word, Second word", result
    )
  }

  @Test
  fun should_return_empty_string_when_word_list_is_empty() {
    //given
    val sut = words("")

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals("the string should be formed from several words", "", result)
  }
}