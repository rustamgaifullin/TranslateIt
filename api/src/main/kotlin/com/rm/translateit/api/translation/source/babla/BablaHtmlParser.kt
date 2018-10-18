package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Words
import com.rm.translateit.api.translation.source.HtmlParser
import org.jsoup.Jsoup


internal class BablaHtmlParser: HtmlParser {
    override fun getTranslateItemsFrom(htmlString: String): Words {
        val document = Jsoup.parse(htmlString)
        val resultElements = document.select("div.content:not(#similarWords):not(#compoundphrases) div.quick-results div.quick-result-overview ul.sense-group-results li a")

        val listOfWords = resultElements
                .map { element -> element.text() }

        return Words(listOfWords)

    }

    override fun getDetailsFrom(htmlString: String): Details {
        val document = Jsoup.parse(htmlString)
        val resultElements = document.select("div.content:not(#similarWords) div.result-block.container div.sense-group div.dict-entry div.dict-translation")

        val detailsResult = resultElements.joinToString("\n") { element ->
            val original = element.select("div.dict-source strong").text()
            val alternative = element.select("div.dict-source span").text()
            val translation = element.select("div.dict-result a[href] strong").text()
            val type = element.select("div.dict-result span.suffix").text()

            "$original$alternative: $translation$type"
        }

        return Details(detailsResult, "")
    }
}