package com.rm.translateit.api.wiki.response

import com.google.gson.annotations.SerializedName

class LanguageLinksResult {
    @SerializedName("langlinks")
    val list: List<LanguageResult> = listOf()
}