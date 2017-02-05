package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.translation.Translator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers



class BablaTranslator(val bablaUrl: BablaUrl, val bablaHtmlParser: BablaHtmlParser): Translator {
    val bablaService: BablaRestService = createBablaService()

    override fun translate(word: String, from: Language, to: Language): Observable<List<TranslationItem>> {
        val url = bablaUrl.construct(word, from, to)

        return bablaService.translate(url)
                .subscribeOn(Schedulers.io())
                .map { responseBody ->
                    bablaHtmlParser.getTranslateItemsFrom(responseBody.string())
                }
    }

    //TODO: set follow redirects to false after introducing proper urls for all languages! See BablaUrl's description
    //TODO: and until they will fix redirects
    private fun createBablaService(): BablaRestService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(interceptor)
                .addInterceptor(redirectErrorInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://bab.la")
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