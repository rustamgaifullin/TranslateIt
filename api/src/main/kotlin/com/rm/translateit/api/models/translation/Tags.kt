package com.rm.translateit.api.models.translation

data class Tags(private val tags: List<String>) {
  companion object {
    fun emptyTags() = Tags(emptyList())

    fun tags(vararg tags: String) = Tags(tags.toList())
  }

  fun toOneLineString(): String {
    if (tags.isEmpty()) return ""

    return tags.reduce { first, second -> "$first, $second" }
  }
}