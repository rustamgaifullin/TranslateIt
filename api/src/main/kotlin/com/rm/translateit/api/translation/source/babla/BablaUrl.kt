package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.translation.source.Url

/**
 * The main reason of moving url logic to this class is to be able to construct proper url for supported languages
 * For example: english - english-polish, polish - polski-angelski, russin - русский-английский
 * It will be possible if language name will be stored for every supported languages.
 */
internal class BablaUrl: Url {
    //TODO: when they fix redirects remove en and add %s parameter
    private val fullUrl = "http://en.bab.la/dictionary/%s/%s"

    override fun construct(word: String, from: Language, to: Language): String {
        val fromTo = createFromTo(from, to)

        return fullUrl.format(fromTo, word)
    }

    fun createFromTo(from: Language, to: Language): String {
        val fromName = from.name.toLowerCase()
        val toName = to.name.toLowerCase()

        return "$fromName-$toName"
    }
}