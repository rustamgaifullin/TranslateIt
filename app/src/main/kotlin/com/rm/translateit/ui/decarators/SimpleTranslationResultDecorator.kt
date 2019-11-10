package com.rm.translateit.ui.decarators

import com.rm.translateit.api.models.translation.Words

class SimpleTranslationResultDecorator : TranslationResultDecorator {
  override fun toSingleLine(words: Words): String {
    return words.toOneLineString()
  }
}