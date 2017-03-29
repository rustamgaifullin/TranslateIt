package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

class LanguageLinksResult {
    @SerializedName("langlinks")
    val list: List<LanguageResult> = listOf()
}