package com.rm.translateit.ui

import androidx.test.filters.SmallTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rm.translateit.ui.util.fromHtml
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