package com.rm.translateit.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.rm.translateit.TranslateApplication
import com.rm.translateit.api.MainComponent

fun Activity.hideKeyboard(condition: () -> Boolean) {
    if (this.currentFocus != null && condition.invoke()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
    }
}

fun Activity.translationComponent(): MainComponent =
        (application as TranslateApplication).translationComponent()
