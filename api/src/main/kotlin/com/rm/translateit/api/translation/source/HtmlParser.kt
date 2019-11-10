package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Words

internal interface HtmlParser {
  fun getTranslateItemsFrom(htmlString: String): Words
  fun getDetailsFrom(htmlString: String): Details
}