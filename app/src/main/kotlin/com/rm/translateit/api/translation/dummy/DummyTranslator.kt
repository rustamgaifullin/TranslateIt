package com.rm.translateit.api.translation.dummy

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.translation.Translator
import rx.Observable
import rx.schedulers.Schedulers

class DummyTranslator : Translator {
    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val translation: TranslationItem
        when (to.code) {
            "en" -> translation = TranslationItem(listOf("Translation"))
            "pl" -> translation = TranslationItem(listOf("Tłumaczenie"))
            "ru" -> translation = TranslationItem(listOf("Перевод"))
            else -> translation = TranslationItem(listOf(""))
        }

        val resultList = listOf(translation)

        return Observable.just(resultList)
                .subscribeOn(Schedulers.immediate())
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.just(listOf())
    }

}