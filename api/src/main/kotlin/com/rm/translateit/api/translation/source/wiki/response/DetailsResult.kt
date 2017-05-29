package com.rm.translateit.api.translation.source.wiki.response

import com.google.gson.annotations.SerializedName

internal data class DetailsResult(
        @SerializedName("extract") val description: String,
        @SerializedName("fullUrl") val url: String)