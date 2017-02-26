package com.rm.translateit.ui

import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
open class ExtensionsTest {

    @Test
    fun shouldReturnHtmlString() {
        //given
        val string:CharSequence = "<br>Example</br>"

        //when
        val result = fromHtml(string)

        //then
        Assert.assertTrue("String should not be empty", result.isNotEmpty())
    }
}