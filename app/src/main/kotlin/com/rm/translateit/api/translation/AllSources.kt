package com.rm.translateit.api.translation

import android.util.Log
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.api.translation.source.Source
import rx.Observable
import rx.lang.kotlin.onError
import javax.inject.Inject

class AllSources @Inject constructor(private val services: Set<Source>): Sources {
    private val TAG = "ServiceModule"

    override fun translate(word: String, from: Language, to: Language): Observable<TranslationResult> {
        return Observable.from(services)
                .flatMap {  provider ->
                    provider.translate(word, from, to)
                            .onError { error -> Log.d(TAG, "error message: $error") }
                            .filter { it.isNotEmpty() }
                            .map { result ->
                                TranslationResult(provider.name(), result)
                            }
                }
    }
}