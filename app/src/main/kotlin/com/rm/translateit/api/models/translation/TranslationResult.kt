package com.rm.translateit.api.models.translation

data class TranslationResult(internal val source: TranslationSource, internal val translation: List<TranslationItem>)