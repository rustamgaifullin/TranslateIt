package com.rm.translateit.api.languages

import android.support.test.InstrumentationRegistry
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class DBLanguagesTest {

    private val sut = DBLanguages()

    companion object {
        @BeforeClass
        fun setUp() {
            val context = InstrumentationRegistry.getTargetContext()
            FlowManager.init(FlowConfig.Builder(context)
                    .build())
        }
    }

    @Test
    fun check_if_database_returns_list_of_languages() {
        val result = sut.languages()

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
    }

    @Test
    fun check_if_database_returns_list_of_origin_languages() {
        val sizeOfAllLanguages = sut.languages().size
        val result = sut.originLanguages()

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
        Assert.assertEquals("Size of languages should be the same with size of origin languages", sizeOfAllLanguages, result.size)
    }

    @Test
    fun check_if_database_returns_proper_list_of_destination_languages() {
        val sizeOfAllLanguages = sut.languages().size
        val result = sut.destinationLanguages("EN")

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
        Assert.assertTrue("List of destination languages should not contain origin language code", result.filter { language -> language.code == "en" }.isEmpty())
        Assert.assertEquals("Size of list of destination languages should be minus one from all languages", sizeOfAllLanguages - 1, result.size)
    }
}