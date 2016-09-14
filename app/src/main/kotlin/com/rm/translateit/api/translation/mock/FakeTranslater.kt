package com.rm.translateit.api.translation.mock

import com.rm.translateit.api.translation.Translater
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class FakeTranslater: Translater {
    override fun translate(word: String, from: String, to: String): Observable<String> {
        val result: String
        when (to) {
            "EN" -> result = "Translation"
            "PL" -> result = "Tłumaczenie"
            "RU" -> result = "Перевод"
            else -> result = ""
        }

        return Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.just(listOf())
    }

}