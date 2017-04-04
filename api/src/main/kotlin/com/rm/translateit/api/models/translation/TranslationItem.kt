package com.rm.translateit.api.models.translation

data class TranslationItem(val words: Words, val tags: Tags = Tags.emptyTags())