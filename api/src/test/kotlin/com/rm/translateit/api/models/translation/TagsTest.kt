package com.rm.translateit.api.models.translation

import com.rm.translateit.api.models.translation.Tags.Companion.emptyTags
import com.rm.translateit.api.models.translation.Tags.Companion.tags
import org.junit.Assert
import org.junit.Test

class TagsTest {
  @Test
  fun should_return_string_from_one_item_in_tag_list() {
    //given
    val sut = tags("One tag")

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals("the string should be equal to one tag", "One tag", result)
  }

  @Test
  fun should_return_string_from_few_items_in_tag_list() {
    //given
    val sut = tags("First tag", "Second tag")

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals(
        "the string should be formed from several tags", "First tag, Second tag", result
    )
  }

  @Test
  fun should_return_empty_string_when_tag_list_is_empty() {
    //given
    val sut = emptyTags()

    //when
    val result = sut.toOneLineString()

    //then
    Assert.assertEquals("the string should be formed from several tags", "", result)
  }
}