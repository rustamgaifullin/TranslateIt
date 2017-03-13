package com.rm.translateit.api.models.translation

data class TranslationResult(internal val source: SourceName, internal val translation: List<TranslationItem>)