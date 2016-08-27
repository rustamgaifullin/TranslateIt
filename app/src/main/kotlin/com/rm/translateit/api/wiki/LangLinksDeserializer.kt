package com.rm.translateit.api.wiki

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rm.translateit.api.wiki.response.LanguageLinksResult
import java.lang.reflect.Type

class LangLinksDeserializer: JsonDeserializer<LanguageLinksResult> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LanguageLinksResult {
        var langlinks = LanguageLinksResult()

        json?.let {
            val query = json.asJsonObject["query"]
            if (query != null) {
                val pages = query.asJsonObject["pages"]
                if (pages != null) {
                    val content = pages.asJsonObject.entrySet().iterator().next().value
                    langlinks = Gson().fromJson(content, LanguageLinksResult::class.java)
                }
            }

        }

        return langlinks
    }

}