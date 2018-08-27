package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.translation.source.Url

class WikiDetailsUrl : Url {
    private val detailsUrl = "https://%s.wikipedia.org/w/api.php?action=query&format=json&prop=extracts|info&exintro=&explaintext=&inprop=url&titles=%s"

    override fun construct(word: String, from: LanguageModel, to: LanguageModel) = detailsUrl.format(to.code.toLowerCase(), word)
}