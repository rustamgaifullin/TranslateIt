package com.rm.translateit.api.models.translation

data class Words(private val listOfWords: List<String>) {
  companion object {
    fun words(vararg word: String) = Words(word.toList())
  }

  fun toOneLineString(): String {
    if (listOfWords.isEmpty()) return ""

    return listOfWords.reduce { first, second -> "$first, $second" }
  }
}