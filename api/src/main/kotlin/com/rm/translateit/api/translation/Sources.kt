package com.rm.translateit.api.translation

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationResult
import rx.Observable

interface Sources {
    fun translate(word: String, from: Language, to: Language): Observable<TranslationResult>
}