package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.translation.Translator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers

class BablaTranslator(val bablaUrl: BablaUrl): Translator {
    val bablaService: BablaRestService = createBablaService()

    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val url = bablaUrl.construct(word, from, to)

        return bablaService.translate(url)
                .subscribeOn(Schedulers.io())
                .map { responseBody ->
                    val htmlString = responseBody.string()
                    val document = Jsoup.parse(htmlString)
                    val resultElements = document.select("div.span6.result-right.row-fluid")

                    resultElements
                            .filter { it.children().size > 0 && it.children().hasClass("result-link") }
                            .map { element ->
                        val results = element.select("a.result-link").first()
                        val tags = element.select("span abbr").map { it.attr("title") }

                        TranslationItem(results.text(), tags)
                    }
                }
    }

    //TODO: set follow redirects to false after introducing proper urls for all languages! See BablaUrl's description
    private fun createBablaService(): BablaRestService {
        val okHttpClient = OkHttpClient.Builder()
                .followRedirects(true)
                .addInterceptor(redirectErrorInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://babla.ru")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        return retrofit.create(BablaRestService::class.java)
    }

    private fun redirectErrorInterceptor(): (Interceptor.Chain) -> Response {
        return { chain ->
            val originalResponse: Response = chain.proceed(chain.request())
            var newResponse = originalResponse
            if (originalResponse.code() == 301) {
                newResponse = originalResponse.newBuilder()
                        .code(200)
                        .build()
            }

            newResponse
        }
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.empty()
    }
}