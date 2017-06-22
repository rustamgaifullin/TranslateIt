package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.TranslationItem

internal interface HtmlParser {
    fun getTranslateItemsFrom(htmlString: String): List<TranslationItem>
    fun getDetailsFrom(htmlString: String): Details
}