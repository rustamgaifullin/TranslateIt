package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.translation.source.Url

internal class WikiUrl: Url {
    private val searchUrl = "https://%s.wikipedia.org/w/api.php?action=query&prop=langlinks&format=json&lllimit=500&redirects&lllang=%s&titles=%s"
    private val detailsUrl = "https://%s.wikipedia.org/w/api.php?action=query&format=json&prop=extracts|info&exintro=&explaintext=&inprop=url&titles=&s"
    
    override fun construct(word: String, from: LanguageModel, to: LanguageModel): String {
        val fromCode = from.code.toLowerCase()
        val toCode = to.code.toLowerCase()

        return searchUrl.format(fromCode, toCode, word)
    }

    fun detailsUrl(word: String, to: LanguageModel): String = detailsUrl.format(to.code.toLowerCase(), word)
}