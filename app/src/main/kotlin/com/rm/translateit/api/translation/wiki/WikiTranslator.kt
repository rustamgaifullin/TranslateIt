package com.rm.translateit.api.translation.wiki

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.translation.Translator
import com.rm.translateit.api.translation.wiki.response.LanguageLinksResult
import com.rm.translateit.api.translation.wiki.response.LanguageResult
import com.rm.translateit.api.translation.wiki.response.SearchResult
import com.rm.translateit.api.translation.wiki.response.SuggestionResult
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers

class WikiTranslator(val url: String) : Translator {

    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val gson = GsonBuilder()
                .registerTypeAdapter(LanguageLinksResult::class.java, LanguageDeserializer())
                .create()
        val service = wikiService(from.code, gson)

        return service.query(word, to.code.toLowerCase())
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
            listOf(TranslationItem(languageResult.title))
        } else {
            emptyList()
        }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        val gson = GsonBuilder()
                .registerTypeAdapter(SuggestionResult::class.java, SuggestionDeserializer())
                .create()

        val service = wikiService(from, gson)

        return service.suggestions(title, offset)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.searchList.map(SearchResult::title)
                }
    }

    private fun wikiService(from: String, gson: Gson?): WikiRestService {
        val retrofit = Retrofit.Builder()
                .baseUrl(url.format(from))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(WikiRestService::class.java)
    }
}