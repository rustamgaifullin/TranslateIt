package com.rm.translateit.api.translation.wiki

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rm.translateit.api.translation.Translater
import com.rm.translateit.api.translation.wiki.response.LanguageLinksResult
import com.rm.translateit.api.translation.wiki.response.SearchResult
import com.rm.translateit.api.translation.wiki.response.SuggestionResult
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class WikiTranslater : Translater {
    override fun translate(word: String, from: String, to: String): Observable<String> {
        val gson = GsonBuilder()
                .registerTypeAdapter(LanguageLinksResult::class.java, LanguageDeserializer())
                .create()
        val service = wikiService(from, gson)

        return service.query(word, to.toLowerCase())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { languageLinksResult ->
                    val languageResult = languageLinksResult.list
                        .filter({ languageResult -> languageResult.code == to.toLowerCase() })
                        .firstOrNull()

                    languageResult?.title ?: ""
                }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        val gson = GsonBuilder()
                .registerTypeAdapter(SuggestionResult::class.java, SuggestionDeserializer())
                .create()

        val service = wikiService(from, gson)

        return service.suggestions(title, offset)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { result ->
                    result.searchList.map(SearchResult::title)
                }
    }

    private fun wikiService(from: String, gson: Gson?): WikiRestService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://$from.wikipedia.org/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(WikiRestService::class.java)
    }
}