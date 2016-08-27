package com.rm.translateit.extension

import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonObject.getOrEmpty(memberName: String): JsonElement {
    var result:JsonElement = JsonObject()
    val element = get(memberName)

    if (element != null) result = element

    return result
}

fun JsonObject.getFirst(): JsonElement {
    var result:JsonElement = JsonObject()

    if (entrySet().size > 0) result = entrySet().iterator().next().value

    return result
}