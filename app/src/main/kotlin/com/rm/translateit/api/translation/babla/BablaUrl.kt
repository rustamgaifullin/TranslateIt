package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language

/**
 * The main reason of moving url logic to this class is to be able to construct proper url for supported languages
 * For example: english - english-polish, polish - polski-angelski, russin - русский-английский
 * It will be possible if language name will be stored for every supported languages.
 */
class BablaUrl {
    private val fullUrl = "http://%s.bab.la/dictionary/%s/%s"

    fun construct(word: String, from: Language, to: Language): String {
        val fromTo = formatLanguages(from, to)
        val fromCode = from.code.toLowerCase()

        return fullUrl.format(fromCode, fromTo, word)
    }

    private fun formatLanguages(from: Language, to: Language): String {
        val fromName = from.name.toLowerCase()
        val toName = to.name.toLowerCase()

        return "$fromName-$toName"
    }
}