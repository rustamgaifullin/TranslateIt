package com.rm.translateit.ui.activities

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.rm.translateit.R
import com.rm.translateit.api.models.translation.TranslationResult
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
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

    @Test
    fun mainActivityTest2() {
        clickOnSpinner(R.id.origin_spinner)
        selectTextInSpinner("Russian")

        clickOnSpinner(R.id.destination_spinner)
        selectTextInSpinner("Polish")

        typeTextAndPressEnter("привет")

        onData(`is`(instanceOf(TranslationResult::class.java)))
                .inAdapterView(allOf(withId(R.id.result_recyclerView)))
                .check(matches(hasDescendant(withText("babla"))))


//        val textView = onView(
//                allOf(withId(R.id.source_textView), withText("babla"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.result_recyclerView),
//                                        0),
//                                0),
//                        isDisplayed()))
//        textView.check(matches(withText("babla")))
//
//        val textView2 = onView(
//                allOf(withId(R.id.translation_textView), withText("pozdrowienie, pozdrowienia [приве́т], {m} \nwitam, witaj, serwus, czołem, witajcie, siema, witamy, dzień dobry, cześć [приве́т], {interj.} \n"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.result_recyclerView),
//                                        0),
//                                1),
//                        isDisplayed()))
//        textView2.check(matches(withText("pozdrowienie, pozdrowienia [приве́т], {m}  witam, witaj, serwus, czołem, witajcie, siema, witamy, dzień dobry, cześć [приве́т], {interj.}  ")))
    }

    private fun clickOnSpinner(spinnerId: Int) {
        val originSpinner = onView(
                allOf(withId(spinnerId),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()))
        originSpinner.perform(click())
    }

    private fun selectTextInSpinner(text: String) {
        val originSpinnerTextView = onView(
                allOf(withId(android.R.id.text1), withText(text), isDisplayed()))
        originSpinnerTextView.perform(click())
    }

    private fun clickOnButton(buttonId: Int) {
        val changeLanguageButton = onView(
                allOf(withId(buttonId),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()))
        changeLanguageButton.perform(click())
    }

    private fun checkSpinnerEqualsToText(spinnerId: Int, text: String) {
        onView(allOf(withId(android.R.id.text1),
                childAtPosition(withId(spinnerId), 0),
                isDisplayed()))
                .check(matches(withText(text)))
    }

    private fun typeTextAndPressEnter(text: String) {
        val appCompatEditText = onView(
                allOf(withId(R.id.word_editText),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()))
        appCompatEditText.perform(replaceText(text), closeSoftKeyboard())
        appCompatEditText.perform(pressImeActionButton())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}