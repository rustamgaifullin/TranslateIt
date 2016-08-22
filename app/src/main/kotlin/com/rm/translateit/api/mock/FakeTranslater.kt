package com.rm.translateit.api.mock

import com.rm.translateit.api.Translater
import com.rm.translateit.api.models.Language

class FakeTranslater: Translater {
    override fun translate(word: String, from: String, to: String): String {
        val result: String
        when (to) {
            "EN" -> result = "FUCK"
            "PL" -> result = "KURWA"
            "RU" -> result = "БЛЯТЬ"
            else -> result = ""
        }

        return result
    }

    override fun languages(): List<Language> {
        return listOf(
                Language("EN", "English"),
                Language("PL", "Polish"),
                Language("RU", "Russian")
        )
    }

}