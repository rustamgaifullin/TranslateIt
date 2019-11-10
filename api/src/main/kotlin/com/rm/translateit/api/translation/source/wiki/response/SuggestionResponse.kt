package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal class SuggestionResponse {
  @SerializedName("Search")
  var searchList: List<SearchResponse> = listOf()
}