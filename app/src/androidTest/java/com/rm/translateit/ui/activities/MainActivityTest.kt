package com.rm.translateit.ui.activities

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.rm.translateit.R
import com.rm.translateit.api.translation.Services
import com.rm.translateit.utils.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
open class MainActivityTest {
    @JvmField
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkLanguageButtonSwapLanguagesProperly() {
        val originLanguage = "English"
        val destinationLanguage = "Polish"

        clickOnSpinner(R.id.origin_spinner)
        selectTextInSpinner(originLanguage)

        clickOnSpinner(R.id.destination_spinner)
        selectTextInSpinner(destinationLanguage)

        clickOnButton(R.id.changeLanguage_button)

        checkSpinnerEqualsToText(R.id.origin_spinner, destinationLanguage)
        checkSpinnerEqualsToText(R.id.destination_spinner, originLanguage)
    }

    @Test
    fun checkDestinationSpinnerShowsCorrectLanguages() {
        val listOfLanguages = Services.languageService().languages().map { it.name }

        listOfLanguages.forEach {
            val originLanguage = it
            clickOnSpinner(R.id.origin_spinner)
            selectTextInSpinner(originLanguage)

            val expectedDestinationLanguages = listOfLanguages.filter { it != originLanguage }
            checkTextInSpinner(R.id.destination_spinner, expectedDestinationLanguages)
            checkTextNotInSpinner(R.id.destination_spinner, originLanguage)
        }
    }
}