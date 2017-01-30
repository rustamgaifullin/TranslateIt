package com.rm.translateit.api.models.translation

data class TranslationItem(val word: String, val tags: List<String> = emptyList()) {
	fun tagsToString(): String {
		if (tags.isEmpty()) return ""
		
		return tags.reduce { first, second -> "$first, $second" }
	}
}