package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.translation.source.HtmlParser
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject


class BablaSource @Inject constructor(private val bablaService: BablaRestService, private val bablaUrl: Url, private val bablaHtmlParser: HtmlParser) : Source {
    override fun name() = SourceName("babla")

    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val url = bablaUrl.construct(word, from, to)

        return bablaService.translate(url)
                .subscribeOn(Schedulers.io())
                .map { responseBody ->
                    bablaHtmlParser.getTranslateItemsFrom(responseBody.string())
                }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.empty()
    }
}