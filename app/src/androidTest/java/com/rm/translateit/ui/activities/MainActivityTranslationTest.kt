package com.rm.translateit.ui.activities

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.rm.translateit.utils.checkResult
import com.rm.translateit.utils.selectLanguages
import com.rm.translateit.utils.typeTextAndPressEnter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
open class MainActivityTranslationTest {
    @JvmField
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        closeSoftKeyboard()
    }

    @Test
    fun testRussianPolishBablaTranslation() {
        selectLanguages("Russian", "Polish")
        typeTextAndPressEnter("сверло")

        checkResult("babla", "świder [сверло́], {n}")
    }

    @Test
    fun testRussianEnglishBablaTranslation() {
        selectLanguages("Russian", "English")
        typeTextAndPressEnter("сверло")

        checkResult("babla", "auger, borer, perforator [сверло́], {n}")
    }

    @Test
    fun testRussianPolishWikiTranslation() {
        selectLanguages("Russian", "Polish")
        typeTextAndPressEnter("сверло")

        checkResult("wikipedia", "Wiertło")
    }

    @Test
    fun testRussianEnglishWIkiTranslation() {
        selectLanguages("Russian", "English")
        typeTextAndPressEnter("сверло")

        checkResult("wikipedia", "Drill bit")
    }

    @Test
    fun testPolishRussianBablaTranslation() {
        selectLanguages("Polish", "Russian")
        typeTextAndPressEnter("nie")

        checkResult("babla", "не, нет {part.}")
    }

    @Test
    fun testPolishEnglishBablaTranslation() {
        selectLanguages("Polish", "English")
        typeTextAndPressEnter("wiertło")

        checkResult("babla", "drill bit {n} bor, świder, aparat, instrument, narzędzie, przyrząd, sprzęt, urządzenie")
    }

    @Test
    fun testPolishRussianWikiTranslation() {
        selectLanguages("Polish", "Russian")
        typeTextAndPressEnter("wiertło")

        checkResult("wikipedia", "Сверло")
    }

    @Test
    fun testPolishEnglishWikiTranslation() {
        selectLanguages("Polish", "English")
        typeTextAndPressEnter("Wiertło")

        checkResult("wikipedia", "Drill bit")
    }
}