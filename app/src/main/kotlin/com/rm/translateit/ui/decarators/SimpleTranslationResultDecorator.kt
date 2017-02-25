package com.rm.translateit.ui.decarators

import com.rm.translateit.api.models.translation.TranslationItem

class SimpleTranslationResultDecorator : TranslationResultDecorator {
    override fun toSingleLine(item: TranslationItem): String {
        val tags = item.tags.toOneLineString()
        val words = item.words.toOneLineString()

        if (tags.isEmpty()) return words

        return "$words <small>$tags</small>"
    }
}