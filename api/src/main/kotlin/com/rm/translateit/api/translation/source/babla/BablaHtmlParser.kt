package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Tags
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words
import com.rm.translateit.api.translation.source.HtmlParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


internal class BablaHtmlParser: HtmlParser {
    override fun getTranslateItemsFrom(htmlString: String): List<TranslationItem> {
        val document = Jsoup.parse(htmlString)
        val resultElements = document.select("div.content:not(#similarWords) div.quick-results div.quick-result-entry:has(.quick-result-overview)")

        return resultElements
                .filter { it.id().isEmpty() && it.allElements.hasClass("sense-group-results") }
                .map { element ->
                    val words = Words(extractTranslatedWords(element))
                    val tags = Tags(extractTags(element))

                    TranslationItem(words, tags)
                }
    }

    private fun extractTranslatedWords(element: Element) = element.select("div.quick-result-overview ul.sense-group-results li a").map { it.text() }

    private fun extractTags(element: Element) = element.select("div.quick-result-option span.suffix").map { it.text() }

    override fun getDetailsFrom(htmlString: String): Details {
        val document = Jsoup.parse(htmlString)
        val resultElements = document.select("div.content:not(#similarWords) div.result-block.container div.sense-group div.dict-entry div.dict-translation")

        val detailsResult = resultElements.joinToString("") { element ->
            val original = element.select("div.dict-source strong").text()
            val alternative = element.select("div.dict-source span").text()
            val translation = element.select("div.dict-result a[href] strong").text()
            val type = element.select("div.dict-result span.suffix").text()

            "$original$alternative: $translation$type\n"
        }

        return Details(detailsResult, "")
    }
}