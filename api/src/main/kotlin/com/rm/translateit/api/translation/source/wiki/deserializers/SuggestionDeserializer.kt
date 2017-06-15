package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rm.translateit.api.extenstion.getOrEmpty
import com.rm.translateit.api.translation.source.wiki.response.SuggestionResponse
import java.lang.reflect.Type

internal class SuggestionDeserializer: JsonDeserializer<SuggestionResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SuggestionResponse {
        var suggestionResult = SuggestionResponse()

        json?.let {
            val content = json.asJsonObject.getOrEmpty("query")

            suggestionResult = Gson().fromJson(content, SuggestionResponse::class.java)
        }

        return suggestionResult
    }
}