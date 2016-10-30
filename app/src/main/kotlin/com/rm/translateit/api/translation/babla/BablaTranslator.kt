package com.rm.translateit.api.translation.babla

import android.util.Log
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.translation.Translator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers

class BablaTranslator(private val url: String): Translator {

    companion object {
        private val TAG = "BablaTranslator"
    }

    override fun translate(word: String, from: Language, to: Language): Observable<String> {
        Log.d(TAG, "$word, ${from.code} -> ${to.code}")
        val bablaService = createBablaService()
        val fromTo = formatLanguages(from, to)
        Log.d(TAG, "fromTo - $fromTo")

        return bablaService.translate(word, fromTo)
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

    private fun createBablaService(): BablaRestService {
        val okHttpClient = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(redirectErrorInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
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

    private fun formatLanguages(from: Language, to: Language): String {
        val fromName = from.name.toLowerCase()
        val toName = to.name.toLowerCase()

        return "$fromName-$toName"
    }

    override fun suggestions(title: String, from: String, offset: Int): Observable<List<String>> {
        return Observable.empty()
    }
}