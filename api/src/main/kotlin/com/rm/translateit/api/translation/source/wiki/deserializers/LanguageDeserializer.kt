package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.rm.translateit.api.extenstion.getOrEmpty
import com.rm.translateit.api.translation.source.wiki.response.LanguageResult
import java.lang.reflect.Type

internal class LanguageDeserializer : WikiJsonDeserializer<LanguageResult>() {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LanguageResult {
        val firstPage = contentFromFirstPage(json)
        val langLinksJson = firstPage
                .asJsonObject.getOrEmpty("langlinks")
                .asJsonArray.first()

        return Gson().fromJson(langLinksJson, LanguageResult::class.java)
    }
}