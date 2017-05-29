package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.rm.translateit.api.translation.source.wiki.response.DetailsResult
import java.lang.reflect.Type

internal class DetailsDeserializer: WikiJsonDeserializer<DetailsResult>() {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DetailsResult {
        val content = contentFromFirstPage(json)
        return Gson().fromJson(content, DetailsResult::class.java)
    }
}