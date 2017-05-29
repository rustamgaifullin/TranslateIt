package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.translation.source.HtmlParser
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject


internal class BablaSource @Inject constructor(private val bablaService: BablaRestService, private val bablaUrl: Url, private val bablaHtmlParser: HtmlParser) : Source {
    override fun name() = SourceName("babla")

    override fun translate(word: String, from: LanguageModel, to: LanguageModel): Observable<Translation> {
        val url = bablaUrl.construct(word, from, to)

        return bablaService.translate(url)
                .subscribeOn(Schedulers.io())
                .map { responseBody ->
                    val translateItems = bablaHtmlParser.getTranslateItemsFrom(responseBody.string())
                    val details = Details("", url)

                    Translation(translateItems, details)
                }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.empty()
    }
}