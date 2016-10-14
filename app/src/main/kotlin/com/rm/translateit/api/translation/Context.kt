package com.rm.translateit.api.translation

import android.util.Log
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.languages.StaticLanguages
import com.rm.translateit.api.translation.dummy.DummyTranslator
import com.rm.translateit.api.translation.models.Language
import com.rm.translateit.api.translation.models.TranslationResult
import com.rm.translateit.api.translation.wiki.WikiTranslator
import rx.Observable
import rx.lang.kotlin.onError

class Context {
    companion object {
        private val TAG = "Context"
        private val url: String = "https://%s.wikipedia.org/"
        private val languageService: Languages = StaticLanguages()
        private val services: List<Pair<String, Translator>> = listOf(
                Pair<String, Translator>("wikipedia", WikiTranslator(url)),
                Pair<String, Translator>("dummy", DummyTranslator())
        )

        fun languages(): List<Language> {
            return languageService.languages()
        }

        fun translate(word: String, from: String, to: String): Observable<TranslationResult> {
            return Observable.from(services).flatMap { item ->
                val (name, service) = item
                service.translate(word, from, to)
                        .onError { error -> Log.d(TAG, "error message: $error") }
                        .filter(String::isNotEmpty)
                        .map { result -> TranslationResult(name, result) }
            }
        }
    }
}