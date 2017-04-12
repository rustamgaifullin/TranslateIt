package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal class LanguageLinksResult {
    @SerializedName("langlinks")
    val list: List<LanguageResult> = listOf()
}