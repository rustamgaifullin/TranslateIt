package com.rm.translateit.api.models.translation

import com.rm.translateit.ui.decarators.TranslationResultDecorator

data class TranslationItem(val words: Words, val tags: Tags = Tags.emptyTags()) {
    fun toOneLine(decorator: TranslationResultDecorator) = decorator.toSingleLine(this)
}