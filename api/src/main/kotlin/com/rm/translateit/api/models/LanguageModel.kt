package com.rm.translateit.api.models

data class LanguageModel(
  var code: String,
  var names: List<NameModel>,
  var dictionary: String = ""
)