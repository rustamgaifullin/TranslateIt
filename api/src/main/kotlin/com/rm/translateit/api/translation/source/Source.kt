package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.TranslationItem
import rx.Observable

internal interface Source {
    fun name(): SourceName
    fun translate(word: String, from: LanguageModel, to: LanguageModel): Observable<List<TranslationItem>>
    fun suggestions(title: String, from: String, offset: Int): Observable<List<String>>
}