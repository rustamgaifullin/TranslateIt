package com.rm.translateit.api.translation.source

import com.rm.translateit.api.models.Language

interface Url {
    fun construct(word: String, from: Language, to: Language): String
}