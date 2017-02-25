package com.rm.translateit.utils

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.view.ViewGroup
import com.rm.translateit.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

fun selectLanguages(originLanguage: String, destinationLanguage: String) {
    clickOnSpinner(R.id.origin_spinner)
    selectTextInSpinner(originLanguage)

    clickOnSpinner(R.id.destination_spinner)
    selectTextInSpinner(destinationLanguage)
}

fun checkResult(title: String, translation: String) {
    Espresso.onView(ViewMatchers.withId(R.id.result_recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(title))))
    Espresso.onView(ViewMatchers.withId(R.id.result_recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(translation))))
}

fun clickOnSpinner(spinnerId: Int) {
    val originSpinner = Espresso.onView(
            Matchers.allOf(ViewMatchers.withId(spinnerId),
                    ViewMatchers.withParent(Matchers.allOf(ViewMatchers.withId(R.id.activity_main),
                            ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content)))),
                    ViewMatchers.isDisplayed()))
    originSpinner.perform(ViewActions.click())
}

fun selectTextInSpinner(text: String) {
    val originSpinnerTextView = Espresso.onView(
            Matchers.allOf(ViewMatchers.withId(android.R.id.text1), ViewMatchers.withText(text), ViewMatchers.isDisplayed()))
    originSpinnerTextView.perform(ViewActions.click())
}

fun clickOnButton(buttonId: Int) {
    val changeLanguageButton = Espresso.onView(
            Matchers.allOf(ViewMatchers.withId(buttonId),
                    ViewMatchers.withParent(Matchers.allOf(ViewMatchers.withId(R.id.activity_main),
                            ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content)))),
                    ViewMatchers.isDisplayed()))
    changeLanguageButton.perform(ViewActions.click())
}

fun checkSpinnerEqualsToText(spinnerId: Int, text: String) {
    Espresso.onView(Matchers.allOf(ViewMatchers.withId(android.R.id.text1),
            childAtPosition(ViewMatchers.withId(spinnerId), 0),
            ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(text)))
}

fun typeTextAndPressEnter(text: String) {
    val appCompatEditText = Espresso.onView(
            Matchers.allOf(ViewMatchers.withId(R.id.word_editText),
                    ViewMatchers.withParent(Matchers.allOf(ViewMatchers.withId(R.id.activity_main),
                            ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content)))),
                    ViewMatchers.isDisplayed()))
    appCompatEditText.perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard())
    appCompatEditText.perform(ViewActions.pressImeActionButton())
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