package com.rm.translateit.api.wiki.response

import com.google.gson.annotations.SerializedName

class SuggestionResult {
    @SerializedName("Search")
    var searchList: List<SearchResult> = listOf()
}