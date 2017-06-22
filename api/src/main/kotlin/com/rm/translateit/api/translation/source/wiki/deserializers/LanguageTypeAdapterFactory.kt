package com.rm.translateit.api.translation.source.wiki.deserializers

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.rm.translateit.api.extenstion.*
import com.rm.translateit.api.translation.source.wiki.response.DetailsResponse
import com.rm.translateit.api.translation.source.wiki.response.LanguageResponse

//TODO: TEST!
internal class LanguageTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        if (LanguageResponse::class.java.isAssignableFrom(type?.rawType)) {
            return LanguageType() as TypeAdapter<T>
        } else if (DetailsResponse::class.java.isAssignableFrom(type?.rawType)) {
            return DetailsType() as TypeAdapter<T>
        } else {
            return null
        }
    }
}

internal class LanguageType : TypeAdapter<LanguageResponse>() {
    override fun read(jsonReader: JsonReader?): LanguageResponse {
        if (jsonReader == null) return LanguageResponse()

        val languageResponse = LanguageResponse()

        with(jsonReader) {
            start {
                nextObject {
                    findFirstArray("langlinks") {
                        beginObject {
                            whileHasNext {
                                update(languageResponse, jsonReader)
                            }
                        }
                    }
                }
            }
        }

        return languageResponse
    }

    private fun update(languageResponse: LanguageResponse, jsonReader: JsonReader) {
        val fieldName = jsonReader.nextName()

        when (fieldName) {
            "lang" -> languageResponse.code = jsonReader.nextString()
            "*" -> languageResponse.title = jsonReader.nextString()
            else -> jsonReader.skipValue()
        }
    }

    override fun write(out: JsonWriter?, value: LanguageResponse?) {
        TODO("Shouldn't be any implementation here, I guess")
    }
}

internal class DetailsType : TypeAdapter<DetailsResponse>() {
    override fun read(jsonReader: JsonReader?): DetailsResponse {
        if (jsonReader == null) return DetailsResponse()

        val detailsResponse = DetailsResponse()

        with(jsonReader) {
            start {
                nextObject {
                    nextObject {
                        nextObject {
                            whileHasNext {
                                update(detailsResponse, jsonReader)
                            }
                        }
                    }
                }

            }
        }

        return detailsResponse
    }

    private fun update(detailsResponse: DetailsResponse, jsonReader: JsonReader) {
        val fieldName = jsonReader.nextName()

        when (fieldName) {
            "extract" -> detailsResponse.description = jsonReader.nextString()
            "fullurl" -> detailsResponse.url = jsonReader.nextString()
            else -> jsonReader.skipValue()
        }
    }

    override fun write(out: JsonWriter?, value: DetailsResponse?) {
        TODO("Shouldn't be any implementation here, I guess")
    }
}