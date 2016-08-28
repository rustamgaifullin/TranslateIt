package com.rm.translateit.api.mock

import com.rm.translateit.api.Translater
import com.rm.translateit.api.models.Language
import rx.Observable

class FakeTranslater: Translater {
    override fun translate(word: String, from: String, to: String): Observable<String> {
        val result: String
        when (to) {
            "EN" -> result = "FUCK"
            "PL" -> result = "KURWA"
            "RU" -> result = "БЛЯТЬ"
            else -> result = ""
        }

        return Observable.just(result)
    }

    override fun languages(): List<Language> {
        return listOf(
                Language("EN", "English"),
                Language("PL", "Polish"),
                Language("RU", "Russian")
        )
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.just(listOf())
    }

}