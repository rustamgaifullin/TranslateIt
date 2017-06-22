package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal class LanguageResponse {
    @SerializedName("lang")
    var code: String = ""

    @SerializedName("*")
    var title: String = ""

    fun isNotEmpty() = code.isNotEmpty() && title.isNotEmpty()
}