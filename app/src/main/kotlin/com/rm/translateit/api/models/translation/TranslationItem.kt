package com.rm.translateit.api.models.translation

data class TranslationItem(val words: List<String>, val tags: List<String> = emptyList()) {
	fun tagsToString(): String {
		if (tags.isEmpty()) return ""
		
		return tags.reduce { first, second -> "$first, $second" }
	}

	fun wordsToString(): String {
		if (words.isEmpty()) return ""

		return words.reduce { first, second -> "$first, $second" }
	}
}