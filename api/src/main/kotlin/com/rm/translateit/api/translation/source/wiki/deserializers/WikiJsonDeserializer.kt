package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.rm.translateit.api.extenstion.getFirst
import com.rm.translateit.api.extenstion.getOrEmpty

internal abstract class WikiJsonDeserializer<T>: JsonDeserializer<T> {
    protected fun contentFromFirstPage(json: JsonElement?): JsonElement {
        json?.let {
            return json.asJsonObject.getOrEmpty("query")
                    .asJsonObject.getOrEmpty("pages")
                    .asJsonObject.getFirst()
        }

        return JsonObject()
    }
}