package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.translation.TranslationItem

interface HtmlParser {
    fun getTranslateItemsFrom(htmlString: String): List<TranslationItem>
}