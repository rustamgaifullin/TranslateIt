package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal data class DetailsResponse(
        @SerializedName("extract") var description: String,
        @SerializedName("fullurl") var url: String)