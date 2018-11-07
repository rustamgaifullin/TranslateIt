package com.rm.translateit.api

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.Name
import com.rm.translateit.api.models.NameModel

fun Language.toLanguage() = LanguageModel(this.code, this.names.map(Name::toName), this.dictionary)
fun Name.toName() = NameModel(this.code, this.name)
