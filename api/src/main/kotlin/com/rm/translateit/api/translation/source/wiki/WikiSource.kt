package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import com.rm.translateit.api.translation.source.wiki.response.DetailsResponse
import com.rm.translateit.api.translation.source.wiki.response.LanguageResponse
import com.rm.translateit.api.translation.source.wiki.response.SearchResponse
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
        val detailsUrl = detailsWikiUrl.construct(word, from, to)

        val search = service.search(url)
                .subscribeOn(Schedulers.io())
        val details = service.details(detailsUrl)
                .subscribeOn(Schedulers.io())

        return Observable.zip(search, details) {languageResult, detailsResult ->
            val translationItems = createResultList(languageResult)
            val details = createDetails(detailsResult)

            Translation(translationItems, details)
        }
    }

    private fun createDetails(detailsResponse: DetailsResponse) = Details(detailsResponse.description, detailsResponse.url)

    private fun createResultList(languageResponse: LanguageResponse?): List<TranslationItem> {
        return if (languageResponse != null) {
            listOf(TranslationItem(words(languageResponse.title)))
        } else {
            emptyList()
        }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return service.suggestions(title, offset)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.searchList.map(SearchResponse::title)
                }
    }
}