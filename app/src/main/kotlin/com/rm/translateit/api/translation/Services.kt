package com.rm.translateit.api.translation

import android.util.Log
import com.rm.translateit.api.languages.DBLanguages
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.api.models.translation.TranslationSource
import com.rm.translateit.api.translation.babla.BablaTranslator
import com.rm.translateit.api.translation.babla.BablaUrl
import com.rm.translateit.api.translation.wiki.WikiTranslator
import com.rm.translateit.api.translation.wiki.WikiUrl
import rx.Observable
import rx.lang.kotlin.onError

//TODO: dependency injection have you heard??? I heard :)
class Services {
    companion object {
        private val TAG = "Services"
        private val languageService: Languages = DBLanguages()
        private val services: List<Pair<TranslationSource, Translator>> = listOf(
                Pair<TranslationSource, Translator>(TranslationSource("wikipedia"), WikiTranslator(WikiUrl())),
                Pair<TranslationSource, Translator>(TranslationSource("babla"), BablaTranslator(BablaUrl()))
//                Pair<TranslationSource, Translator>(TranslationSource("dummy"), DummyTranslator())
        )

        fun languageService(): Languages {
            return languageService
        }

        fun translate(word: String, from: Language, to: Language): Observable<TranslationResult> {
            return Observable.from(services)
                    .flatMap { item ->
                        val (name, service) = item
                        service.translate(word, from, to)
                                .onError { error -> Log.d(TAG, "error message: $error") }
                                .filter { it.isNotEmpty() }
                                .map { result ->
                                    TranslationResult(name, result)
                                }
                    }
        }
    }
}