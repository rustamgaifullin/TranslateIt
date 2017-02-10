package com.rm.translateit.api.translation

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import rx.Observable

interface Translator {
    fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>>
    fun suggestions(title: String, from: String, offset: Int): Observable<List<String>>
}