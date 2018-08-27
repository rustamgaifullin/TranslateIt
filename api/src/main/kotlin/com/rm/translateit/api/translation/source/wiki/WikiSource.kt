package com.rm.translateit.api.translation.source.wiki

import com.jayway.jsonpath.JsonPath
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import com.rm.translateit.api.translation.source.wiki.response.LanguageResponse
import com.rm.translateit.api.translation.source.wiki.response.SearchResponse
import net.minidev.json.JSONArray
import okhttp3.ResponseBody
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

internal class WikiSource @Inject constructor(
        private val wikiUrl: Url,
        private val detailsWikiUrl: Url,
        private val service: WikiRestService) : Source {
    override fun name() = SourceName("wikipedia")

    override fun translate(word: String, from: LanguageModel, to: LanguageModel): Observable<Translation> {
        val url = wikiUrl.construct(word, from, to)

        return service.search(url)
                .subscribeOn(Schedulers.io())
                .map { t: ResponseBody? -> t?.string() ?: "" }
                .filter { it.isNotEmpty() }
                .map { JsonPath.read<Map<*, *>>(it, "$") }
                .map {
                    it.getOrNull("query")
                            ?.getOrNull("pages")
                            ?.getFirst("-1")
                            ?.getJsonArray("langlinks")
                            ?.getFirstObject()
                }
                .filter { it != null}
                .map { it!! }
                .flatMap { responseMap ->
                    val title = responseMap["*"].toString()
                    val code = responseMap["lang"].toString()
                    val languageResponse = LanguageResponse(code, title)

                    val detailsUrl = detailsWikiUrl.construct(title, from, to)

                    service.details(detailsUrl)
                            .map { t: ResponseBody? -> t?.string() ?: "" }
                            .filter { it.isNotEmpty() }
                            .map(toTranslation(languageResponse))
                }
    }

    private fun toTranslation(languageResult: LanguageResponse): (String) -> Translation {
        return { response ->
            val translationItems = createResultList(languageResult)
            val details = createDetails(response)

            Translation(translationItems, details)
        }
    }

    private fun createResultList(languageResponse: LanguageResponse) = listOf(TranslationItem(words(languageResponse.title)))

    private fun createDetails(json: String): Details {
        val map = JsonPath.read<Map<*, *>>(json, "$")
        val result = map.getOrNull("query")
                ?.getOrNull("pages")
                ?.getFirst("-1")

        val description = result?.get("extract") as String? ?: ""
        val url = result?.get("fullurl") as String? ?: ""

        return Details(description, url)
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return service.suggestions(title, offset)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.searchList.map(SearchResponse::title)
                }
    }

    private fun Map<*, *>.getOrNull(key: String): Map<*, *>? {
        if (containsKey(key)) {
            return get(key) as Map<*, *>
        }

        return null
    }

    private fun Map<*, *>.getFirst(excludedKey: String): Map<*, *>? {
        val key = keys.first()

        if (key == excludedKey) return null

        return get(key) as Map<*, *>
    }

    private fun Map<*, *>.getJsonArray(key: String): JSONArray? {
        if (containsKey(key)) {
            return get(key) as JSONArray?
        }

        return null
    }

    private fun JSONArray.getFirstObject(): Map<*, *>? {
        if (size > 0) {
            return get(0) as Map<*, *>
        }

        return null
    }
}