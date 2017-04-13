package com.rm.translateit.api.translation

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.TranslationResult
import rx.Observable

interface Sources {
    fun translate(word: String, from: LanguageModel, to: LanguageModel): Observable<TranslationResult>
}