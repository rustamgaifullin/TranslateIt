package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal class LanguageResult {
    @SerializedName("lang")
    var code: String = ""

    @SerializedName("*")
    var title: String = ""
}