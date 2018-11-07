package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.translation.source.Url

/**
 * The main reason of moving url logic to this class is to be able to construct proper url for supported languages
 * For example: english - english-polish, polish - polski-angelski, russin - русский-английский
 * It will be possible if language name will be stored for every supported languages.
 */
internal class BablaUrl: Url {
    companion object {
        private const val RUSSIAN_CODE = "ru"
    }

    private val fullUrl = "https://%s.bab.la/%s/%s/%s"
    private val russianUrl = "https://www.babla.ru/%s/%s"

    override fun construct(word: String, from: LanguageModel, to: LanguageModel): String {
        val fromTo = createFromTo(from, to)
        val wordWithoutSpaces = word.replace(" ", "-")

        if (from.code.toLowerCase() == RUSSIAN_CODE) {
            return russianUrl.format(fromTo, wordWithoutSpaces)
        }

        return fullUrl.format(from.code.toLowerCase(), from.dictionary, fromTo, wordWithoutSpaces)
    }

    private fun createFromTo(from: LanguageModel, to: LanguageModel): String {
        val names = from.names
        val fromName = names.first { nameModel -> nameModel.code.equals(from.code, true)}.name.toLowerCase()
        val toName = names.first { nameModel -> nameModel.code.equals(to.code, true) }.name.toLowerCase()

        return "$fromName-$toName"
    }
}