package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.translation.source.HtmlParser
import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import javax.inject.Singleton

@Module
internal class BablaModule {

    @Provides
    @Singleton
    fun url() = BablaUrl()


    @Provides
    @Singleton
    fun parser(): HtmlParser = BablaHtmlParser()

    @Provides @IntoSet
    @Singleton
    fun service(restService: BablaRestService, url: BablaUrl, htmlParser: HtmlParser): Source
            = BablaSource(restService, url, htmlParser)

    @Provides
    @Singleton
    fun restService(okHttpClient: OkHttpClient) = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://bab.la")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(BablaRestService::class.java)

    @Provides
    @Singleton
    fun okHttpClient(interceptor: Interceptor) = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(interceptor)
                .build()

    @Provides
    @Singleton
    fun interceptor(): Interceptor = BablaInterceptor()
}