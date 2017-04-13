package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.LanguageModel

internal interface Url {
    fun construct(word: String, from: LanguageModel, to: LanguageModel): String
}