package com.rm.translateit.api.translation

import com.rm.translateit.api.logger.Logger
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.api.translation.source.Source
import rx.Observable
import rx.lang.kotlin.onError
import javax.inject.Inject

internal class AllSources @Inject constructor(private val services: Set<Source>, private val logger: Logger): Sources {
    override fun translate(word: String, from: LanguageModel, to: LanguageModel): Observable<TranslationResult> {
        return Observable.from(services)
                .flatMap {  provider ->
                    provider.translate(word, from, to)
                            .onError { error -> logger.debug("error message: $error") }
                            .map { result ->
                                TranslationResult(provider.name(), result)
                            }
                }
    }
}