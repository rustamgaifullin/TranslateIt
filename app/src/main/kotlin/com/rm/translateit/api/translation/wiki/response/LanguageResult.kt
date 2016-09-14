package com.rm.translateit.api.translation.wiki.response

import com.google.gson.annotations.SerializedName

class LanguageResult {
    @SerializedName("lang")
    var code: String = ""

    @SerializedName("*")
    var title: String = ""
}