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
                .map { t: ResponseBody? -> t?.string() ?: ""}
                .filter { it.isNotEmpty() }
                .map { JsonPath.read<Map<*, *>>(it, "$.query.pages")} // reading pages object first
                .filter { it.isNotEmpty() && it.keys.isNotEmpty() && it.keys.first() != "-1" } //check if it's present and keys are present and first key is not -1
                .map { (it[it.keys.first()] as Map<*, *>)["langlinks"] as JSONArray } //getting langlinks object
                .filter { it.isNotEmpty() } //checking if it's not empty
                .map { it[0] as Map<*, *> } //getting first object from the map because it should be only one
                .filter { it.contains("*") && it.contains("lang") } //checking if map contains all required keys
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
        val description = JsonPath.read<JSONArray>(json, "$.query.pages.*.extract")[0].toString()
        val url = JsonPath.read<JSONArray>(json, "$.query.pages.*.fullurl")[0].toString()

        return Details(description, url)
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return service.suggestions(title, offset)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.searchList.map(SearchResponse::title)
                }
    }
}