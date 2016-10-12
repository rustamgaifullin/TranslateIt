package com.rm.translateit.api.translation

import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.languages.StaticLanguages
import com.rm.translateit.api.translation.mock.FakeTranslater
import com.rm.translateit.api.translation.models.Language
import com.rm.translateit.api.translation.models.TranslationResult
import com.rm.translateit.api.translation.wiki.WikiTranslator
import rx.Observable

class Context {
    companion object {
        private val url: String = "https://%s.wikipedia.org/"
        private val languageService: Languages = StaticLanguages()
        private val services: List<Pair<String, Translater>> = listOf(
                Pair<String, Translater>("wikipedia", WikiTranslator(url)),
                Pair<String, Translater>("fake", FakeTranslater())
        )

        fun languages(): List<Language> {
            return languageService.languages()
        }

        fun translate(word: String, from: String, to: String): Observable<TranslationResult> {
            return Observable.from(services).flatMap { item ->
                val (name, service) = item
                service.translate(word, from, to)
                        .filter(String::isNotEmpty)
                        .map { result -> TranslationResult(name, result) }
            }
        }
    }
}