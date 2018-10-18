package com.rm.translateit.ui.decarators

import com.rm.translateit.api.models.translation.Words

interface TranslationResultDecorator {
    fun toSingleLine(words: Words): String
}