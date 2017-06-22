package com.rm.translateit.api.translation.source.wiki.response

internal data class LanguageResponse (var code: String = "", var title: String = "") {
    fun isNotEmpty() = code.isNotEmpty() && title.isNotEmpty()
}