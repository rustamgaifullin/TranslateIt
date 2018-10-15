package com.rm.translateit.api.languages

import android.support.test.InstrumentationRegistry
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.rm.translateit.api.models.Language
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class DBLanguagesTest {
    private val sut = DBLanguages()

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUp() {
            val context = InstrumentationRegistry.getTargetContext()
            FlowManager.init(FlowConfig.Builder(context)
                    .build())
        }
    }

    @Test
    fun check_if_database_returns_list_of_languages() {
        val result = sut.all()

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
    }

    @Test
    fun check_if_database_returns_list_of_origin_languages() {
        val sizeOfAllLanguages = sut.all().size
        val result = sut.originLanguages()

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
        Assert.assertEquals("Size of languages should be the same with size of origin languages", sizeOfAllLanguages, result.size)
    }

    @Test
    fun check_if_database_returns_proper_list_of_destination_languages() {
        val sizeOfAllLanguages = sut.all().size
        val result = sut.destinationLanguages("EN")

        Assert.assertTrue("List of languages should not be empty", result.isNotEmpty())
        Assert.assertTrue("List of destination languages should not contain origin language code", result.filter { language -> language.code == "en" }.isEmpty())
        Assert.assertEquals("Size of list of destination languages should be minus one from all languages", sizeOfAllLanguages - 1, result.size)
    }

    @Test
    fun check_updating_origin_last_usage() {
        val languages = sut.originLanguages()
        val languageToUpdate = languages.last()

        sut.updateOriginLastUsage(languageToUpdate)

        val result = sut.originLanguages()

        Assert.assertTrue("First language should be last updated", result.first() == languageToUpdate)
    }

    @Test
    fun check_updating_destination_last_usage() {
        val languages = sut.originLanguages()
        val languageToUpdate = languages.last()

        sut.updateDestinationLastUsage(languageToUpdate)

        val result = sut.destinationLanguages("")

        Assert.assertTrue("First language should be last updated", result.first() == languageToUpdate)
    }

    @Test
    fun check_getting_all_names_for_a_language() {
        val languages = sut.all()

        languages.forEach {
            val names = it.names

            Assert.assertTrue(names.isNotEmpty())
        }
    }

    @Test
    fun check_finding_name_for_a_language() {
        val languages = sut.all()

        Assert.assertEquals("russian", getName(languages, "ru", "en"))
        Assert.assertEquals("polish", getName(languages, "pl", "en"))
        Assert.assertEquals("english", getName(languages, "en", "en"))
        Assert.assertEquals("spanish", getName(languages, "es", "en"))
        Assert.assertEquals("русский", getName(languages, "ru", "ru"))
        Assert.assertEquals("польский", getName(languages, "pl", "ru"))
        Assert.assertEquals("английский", getName(languages, "en", "ru"))
        Assert.assertEquals("испанский", getName(languages, "es", "ru"))
        Assert.assertEquals("rosyjski", getName(languages, "ru", "pl"))
        Assert.assertEquals("polski", getName(languages, "pl", "pl"))
        Assert.assertEquals("angielski", getName(languages, "en", "pl"))
        Assert.assertEquals("hiszpanski", getName(languages, "es", "pl"))
        Assert.assertEquals("ruso", getName(languages, "ru", "es"))
        Assert.assertEquals("polaco", getName(languages, "pl", "es"))
        Assert.assertEquals("ingles", getName(languages, "en", "es"))
        Assert.assertEquals("espanol", getName(languages, "es", "es"))
    }

    @Test
    fun return_default_names_for_unknown_locales() {
        val languages = sut.all()

        Assert.assertEquals("russian", getName(languages, "ru", "cn"))
        Assert.assertEquals("polish", getName(languages, "pl", "kk"))
        Assert.assertEquals("english", getName(languages, "en", "lol"))
        Assert.assertEquals("spanish", getName(languages, "es", "what"))
    }

    private fun getName(languages: List<Language>, forLanguage: String, locale: String): String {
        return languages
                .first { it.code.equals(forLanguage, true) }
                .findName(locale)
    }
}