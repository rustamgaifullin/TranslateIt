package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.SourceName
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.translation.source.HtmlParser
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.Url
import okhttp3.ResponseBody
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

internal class BablaSource @Inject constructor(
  private val bablaService: BablaRestService,
  private val bablaUrl: Url,
  private val bablaHtmlParser: HtmlParser
) : Source {
  override fun name() = SourceName("babla")

  override fun translate(
    word: String,
    from: LanguageModel,
    to: LanguageModel
  ): Observable<Translation> {
    val url = bablaUrl.construct(word, from, to)

    return bablaService.translate(url)
        .subscribeOn(Schedulers.io())
        .map(toTranslation())
        .filter {
          it.words.toOneLineString()
              .isNotEmpty()
        }
  }

  private fun toTranslation(): (ResponseBody) -> Translation {
    return { responseBody ->
      val htmlString = responseBody.string()

      val translateItems = bablaHtmlParser.getTranslateItemsFrom(htmlString)
      val details = bablaHtmlParser.getDetailsFrom(htmlString)

      Translation(translateItems, details)
    }
  }

  override fun suggestions(
    title: String,
    from: String,
    offset: Int
  ): Observable<List<String>> {
    return Observable.empty()
  }
}