package com.rm.translateit.api.models.translation

import org.junit.Assert
import org.junit.Test

class TranslationItemTest {
    @Test
    fun should_return_string_from_one_item_in_tag_list() {
        //given
        val sut = TranslationItem("", listOf("One tag"))
        
        //when
        val result = sut.tagsToString()
        
        //then
        Assert.assertEquals("the string should be equal to one tag", "One tag", result)
    }
    
    @Test
    fun should_return_string_from_few_items_in_tag_list() {
        //given
        val sut = TranslationItem("", listOf("First tag", "Second tag"))
        
        //when
        val result = sut.tagsToString()
        
        //then
        Assert.assertEquals("the string should be formed from several tags", "First tag, Second tag", result)
    }
    
    @Test
    fun should_return_empty_string_when_tag_list_is_empty() {
        //given
        val sut = TranslationItem("") 
        
        //when
        val result = sut.tagsToString()
        
        //then
        Assert.assertEquals("the string should be formed from several tags", "", result)
    }
}