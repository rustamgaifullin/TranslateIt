package com.rm.translateit.api.translation.wiki

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rm.translateit.api.translation.wiki.response.SuggestionResult
import com.rm.translateit.extension.getOrEmpty
import java.lang.reflect.Type

class SuggestionDeserializer: JsonDeserializer<SuggestionResult> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SuggestionResult {
        var suggestionResult = SuggestionResult()

        json?.let {
            val content = json.asJsonObject.getOrEmpty("query")

            suggestionResult = Gson().fromJson(content, SuggestionResult::class.java)
        }

        return suggestionResult
    }
}