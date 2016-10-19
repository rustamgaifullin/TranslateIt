package com.rm.translateit.api.translation

import com.rm.translateit.api.models.Language
import rx.Observable

interface Translator {
    fun translate(word: String, from: Language, to: Language): Observable<String>
    fun suggestions(title: String, from: String, offset: Int): Observable<List<String>>
}