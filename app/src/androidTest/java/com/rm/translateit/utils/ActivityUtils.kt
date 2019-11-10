package com.rm.translateit.utils

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.rm.translateit.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

fun selectLanguages(
  originLanguage: String,
  destinationLanguage: String
) {
  clickOnSpinner(R.id.origin_spinner)
  selectTextInSpinner(originLanguage)

  clickOnSpinner(R.id.destination_spinner)
  selectTextInSpinner(destinationLanguage)
}

fun checkResult(
  title: String,
  translation: String
) {
  onView(withId(R.id.resultRecyclerView))
      .check(matches(hasDescendant(withText(title))))
  onView(withId(R.id.resultRecyclerView))
      .check(matches(hasDescendant(withText(translation))))
}

fun checkTextInSpinner(
  spinnerId: Int,
  texts: List<String>
) {
  for (text in texts) {
    onData(withText(text))
        .inAdapterView(withId(spinnerId))
  }
}

fun checkTextNotInSpinner(
  spinnerId: Int,
  exceptText: String
) {
  onData(Matchers.not(withText(exceptText)))
      .inAdapterView(withId(spinnerId))
}

fun clickOnSpinner(spinnerId: Int) {
  val originSpinner = onView(
      allOf(
          withId(spinnerId),
          withParent(
              allOf(
                  withId(R.id.activity_main),
                  withParent(withId(android.R.id.content))
              )
          ),
          isDisplayed()
      )
  )
  originSpinner.perform(click())
}

fun selectTextInSpinner(text: String) {
  val originSpinnerTextView = onView(
      allOf(withId(android.R.id.text1), withText(text), isDisplayed())
  )
  originSpinnerTextView.perform(click())
}

fun clickOnButton(buttonId: Int) {
  val changeLanguageButton = onView(
      allOf(
          withId(buttonId),
          withParent(
              allOf(
                  withId(R.id.activity_main),
                  withParent(withId(android.R.id.content))
              )
          ),
          isDisplayed()
      )
  )
  changeLanguageButton.perform(click())
}

fun checkSpinnerEqualsToText(
  spinnerId: Int,
  text: String
) {
  onView(
      allOf(
          withId(android.R.id.text1),
          childAtPosition(withId(spinnerId), 0),
          isDisplayed()
      )
  )
      .check(matches(withText(text)))
}

fun typeTextAndPressEnter(text: String) {
  val appCompatEditText = onView(
      allOf(
          withId(R.id.wordEditText),
          withParent(
              allOf(
                  withId(R.id.activity_main),
                  withParent(withId(android.R.id.content))
              )
          ),
          isDisplayed()
      )
  )
  appCompatEditText.perform(replaceText(text), closeSoftKeyboard())
  appCompatEditText.perform(pressImeActionButton())
}

private fun childAtPosition(
  parentMatcher: Matcher<View>,
  position: Int
): Matcher<View> {

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