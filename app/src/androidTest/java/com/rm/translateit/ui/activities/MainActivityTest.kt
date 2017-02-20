package com.rm.translateit.ui.activities

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.rm.translateit.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
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