package com.rm.translateit.api.extenstion

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

//TODO: Should be moved as part of the KSON library ;)
internal fun JsonObject.getOrEmpty(memberName: String): JsonElement {
    var result: JsonElement = JsonObject()
    val element = get(memberName)

    if (element != null) result = element

    return result
}

internal fun JsonObject.getFirst(): JsonElement {
    var result: JsonElement = JsonObject()

    if (entrySet().size > 0) result = entrySet().iterator().next().value

    return result
}

internal inline fun JsonReader.start(body: () -> Unit) {
    beginObject {
        while (hasNext()) {
            body()
        }
    }
}

internal inline fun JsonReader.beginObject(body: () -> Unit) {
    beginObject()
    body()
    endObject()
}

internal inline fun JsonReader.beginArray(body: () -> Unit) {
    beginArray()
    body()
    endArray()
}

internal inline fun JsonReader.nextObject(body: () -> Unit) {
    skipName()

    if (peek() == JsonToken.BEGIN_OBJECT) {
        beginObject(body)
    } else {
        skipValue()
    }
}

internal fun JsonReader.skipName() {
    if (peek() == JsonToken.NAME) {
        nextName()
    }
}

internal fun JsonReader.findArray(value: String, found: () -> Unit) {
    findArray(false, value, found)
}

internal fun JsonReader.findFirstArray(value: String, found: () -> Unit) {
    findArray(true, value, found)
}

private fun JsonReader.findArray(stopOnFirst: Boolean, value: String, found: () -> Unit) {
    while (hasNext()) {
        if (peek() == JsonToken.BEGIN_ARRAY) {
            beginArray {
                findArray(stopOnFirst, value, found)
            }
        } else if (peek() == JsonToken.BEGIN_OBJECT) {
            beginObject {
                findArray(stopOnFirst, value, found)
            }
        } else if (nextName() == value) {
            beginArray {
                found()
            }

            if (stopOnFirst) return
        } else {
            skipValue()
        }
    }
}

internal fun JsonReader.whileHasNext(body: () -> Unit) {
    while (hasNext()) {
        body()
    }
}