package com.rm.translateit.ui.activities

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.rm.translateit.R
import com.rm.translateit.utils.checkSpinnerEqualsToText
import com.rm.translateit.utils.clickOnButton
import com.rm.translateit.utils.clickOnSpinner
import com.rm.translateit.utils.selectTextInSpinner
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
    fun testChangeLanguageButton() {
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
}