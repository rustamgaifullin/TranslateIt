package com.rm.translateit.api.translation.dummy

import com.rm.translateit.api.translation.Translator
import com.rm.translateit.api.models.Language
import rx.Observable
import rx.schedulers.Schedulers

class DummyTranslator : Translator {
    override fun translate(word: String, from: Language, to: Language): Observable<String> {
        val result: String
        when (to.code) {
            "EN" -> result = "Translation"
            "PL" -> result = "Tłumaczenie"
            "RU" -> result = "Перевод"
            else -> result = ""
        }

        return Observable.just(result)
                .subscribeOn(Schedulers.immediate())
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.just(listOf())
    }

}