package com.rm.translateit.api.translation.source.wiki

import com.google.gson.GsonBuilder
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import com.rm.translateit.api.translation.source.wiki.deserializers.LanguageDeserializer
import com.rm.translateit.api.translation.source.wiki.response.LanguageLinksResult
import com.rm.translateit.api.translation.source.wiki.response.LanguageResult
import com.rm.translateit.api.translation.source.wiki.response.SearchResult
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class WikiSource @Inject constructor(private val wikiUrl: Url) : Source {
    private val service = wikiService()

    override fun name() = SourceName("wikipedia")

    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val url = wikiUrl.construct(word, from, to)
        
        return service.query(url)
                .subscribeOn(Schedulers.io())
                .map { languageLinksResult ->
                    val languageResult = languageLinksResult.list
                        .filter({ languageResult -> languageResult.code == to.code.toLowerCase() })
                        .firstOrNull()

                    createResultList(languageResult)
                }
    }

    private fun createResultList(languageResult: LanguageResult?): List<TranslationItem> {
        return if (languageResult != null) {
            listOf(TranslationItem(words(languageResult.title)))
        } else {
            emptyList()
        }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return service.suggestions(title, offset)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.searchList.map(SearchResult::title)
                }
    }

    private fun wikiService(): WikiRestService {
        val gson = GsonBuilder()
                .registerTypeAdapter(LanguageLinksResult::class.java, LanguageDeserializer())
                .create()
                
        val retrofit = Retrofit.Builder()
                .baseUrl("http://wikipedia.org")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(WikiRestService::class.java)
    }
}