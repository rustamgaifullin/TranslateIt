package com.rm.translateit.ui.decarators

import com.rm.translateit.api.models.translation.TranslationItem

interface TranslationResultDecorator {
    fun toSingleLine(item: TranslationItem): String
}