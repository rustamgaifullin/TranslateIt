package com.rm.translateit.api.wiki.response

import com.google.gson.annotations.SerializedName
import java.util.Collections.*

class LanguageLinksResult {

    @SerializedName("langlinks")
    val list: List<LanguageResult> = emptyList()
}