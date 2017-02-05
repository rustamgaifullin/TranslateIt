package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.translation.TranslationItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class BablaHtmlParser {
    fun getTranslateItemsFrom(htmlString: String): List<TranslationItem> {
        val document = Jsoup.parse(htmlString)
        val resultElements = document.select("div.content:not(#similarWords) div.quick-results div.quick-result-entry:has(.quick-result-overview)")

        return resultElements
                .filter { it.id().isEmpty() && it.allElements.hasClass("sense-group-results") }
                .map { element ->
                    val results = extractTranslatedWords(element)
                    val tags = extractTags(element)

                    TranslationItem(results, tags)
                }
    }

    private fun extractTranslatedWords(element: Element) = element.select("div.quick-result-overview ul.sense-group-results li a").map { it.text() }

    private fun extractTags(element: Element) = element.select("div.quick-result-option span.suffix").map { it.text() }
}