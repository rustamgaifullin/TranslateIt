package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal class SearchResponse {
  @SerializedName("title")
  var title: String = ""
}