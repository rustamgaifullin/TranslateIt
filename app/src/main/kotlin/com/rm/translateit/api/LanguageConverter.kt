package com.rm.translateit.api

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.LanguageModel

fun LanguageModel.toLanguage() = Language(this.code, this.name)
