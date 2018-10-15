package com.rm.translateit.ui.extension

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import com.rm.translateit.TranslateApplication
import com.rm.translateit.api.MainComponent
import java.util.*

fun Activity.hideKeyboard(condition: () -> Boolean) {
    if (this.currentFocus != null && condition.invoke()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
    }
}

fun Activity.translationComponent(): MainComponent =
        (application as TranslateApplication).translationComponent()

fun Context.currentLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }
}