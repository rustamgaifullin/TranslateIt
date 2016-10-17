package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.translation.Translator
import com.rm.translateit.api.translation.models.Language
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers

class BablaTranslator(private val url: String): Translator {

    override fun translate(word: String, from: Language, to: Language): Observable<String> {
        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val bablaService = retrofit.create(BablaRestService::class.java)

        val fromTo = formatLanguages(from, to)

        return bablaService.query(fromTo, word)
                .subscribeOn(Schedulers.io())
                .map { responseBody ->
                    val htmlString = responseBody.string()
                    val document = Jsoup.parse(htmlString)
                    val resultElements = document.select("div.span6.result-right.row-fluid")
                    val translationElements = resultElements.select("a.result-link")

                    translationElements
                            .map(Element::text)
                            .firstOrNull() //TODO: yeah...bad design, babla can return multiple results but I can handle only one :(
                            .orEmpty()
                }
    }

    private fun formatLanguages(from: Language, to: Language): String {
        val fromName = from.name.toLowerCase()
        val toName = to.name.toLowerCase()

        return "$fromName-$toName"
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.empty()
    }
}