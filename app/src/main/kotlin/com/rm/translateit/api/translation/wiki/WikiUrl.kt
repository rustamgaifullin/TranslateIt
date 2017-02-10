package com.rm.translateit.api.translation.wiki

import com.rm.translateit.api.models.Language

class WikiUrl {
    private val fullUrl = "https://%s.wikipedia.org/w/api.php?action=query&prop=langlinks&format=json&lllimit=500&redirects&lllang=%s&titles=%s"
    
    fun construct(word: String, from: Language, to: Language): String {
        val fromCode = from.code.toLowerCase()
        val toCode = to.code.toLowerCase()

        return fullUrl.format(fromCode, toCode, word)
    }
}