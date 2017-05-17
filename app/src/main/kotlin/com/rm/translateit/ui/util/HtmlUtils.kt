package com.rm.translateit.ui.util

import android.annotation.TargetApi
import android.os.Build
import android.text.Html
import android.text.Spanned

@Suppress("DEPRECATION")
fun fromHtml(text: CharSequence): String {
    val textString = text.toString()
    val htmlText: Spanned

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        htmlText = Html.fromHtml(textString)
    } else {
        htmlText = fromHtmlNougat(textString, Html.FROM_HTML_MODE_COMPACT)
    }

    return htmlText.toString()
}

@TargetApi(Build.VERSION_CODES.N)
private fun fromHtmlNougat(text: String, flag: Int): Spanned {
    return Html.fromHtml(text, flag)
}