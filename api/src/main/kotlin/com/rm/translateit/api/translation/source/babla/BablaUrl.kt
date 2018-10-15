package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.translation.source.Url

/**
 * The main reason of moving url logic to this class is to be able to construct proper url for supported languages
 * For example: english - english-polish, polish - polski-angelski, russin - русский-английский
 * It will be possible if language name will be stored for every supported languages.
 */
internal class BablaUrl: Url {
    private val fullUrl = "https://%s.bab.la/dictionary/%s/%s"
    private val russianUrl = "https://www.babla.ru/%s/%s"

    override fun construct(word: String, from: LanguageModel, to: LanguageModel): String {
        val fromTo = createFromTo(from, to)

        if (from.code.toLowerCase() == "ru") {
            return russianUrl.format(fromTo, word)
        }

        return fullUrl.format(from.code, fromTo, word)
    }

    fun createFromTo(from: LanguageModel, to: LanguageModel): String {
        val names = from.names
        val fromName = names.first { nameModel -> nameModel.code == from.code }.name.toLowerCase()
        val toName = names.first { nameModel -> nameModel.code == to.code }.name.toLowerCase()

        return "$fromName-$toName"
    }
}