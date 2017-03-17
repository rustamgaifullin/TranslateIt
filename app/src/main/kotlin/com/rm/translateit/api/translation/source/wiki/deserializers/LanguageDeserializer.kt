package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rm.translateit.api.translation.source.wiki.response.LanguageLinksResult
import com.rm.translateit.extension.getFirst
import com.rm.translateit.extension.getOrEmpty
import java.lang.reflect.Type

class LanguageDeserializer : JsonDeserializer<LanguageLinksResult> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LanguageLinksResult {
        var langlinks = LanguageLinksResult()

        json?.let {
            val content = json.asJsonObject.getOrEmpty("query")
                    .asJsonObject.getOrEmpty("pages")
                    .asJsonObject.getFirst()
            langlinks = Gson().fromJson(content, LanguageLinksResult::class.java)
        }

        return langlinks
    }

}