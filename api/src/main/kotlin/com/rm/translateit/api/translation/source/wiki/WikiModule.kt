package com.rm.translateit.api.translation.source.wiki

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.wiki.deserializers.LanguageDeserializer
import com.rm.translateit.api.translation.source.wiki.response.LanguageResult
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
internal class WikiModule {

    @Provides
    @Singleton
    fun url() = WikiUrl()

    @Provides
    @Singleton
    fun detailsUrl() = WikiDetailsUrl()

    @Provides @IntoSet
    @Singleton
    fun service(url: WikiUrl, detailsUrl: WikiDetailsUrl, restService: WikiRestService): Source = WikiSource(url, detailsUrl, restService)

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder()
            .registerTypeAdapter(LanguageResult::class.java, LanguageDeserializer())
            .create()

    @Provides
    @Singleton
    fun restService(gson: Gson): WikiRestService = Retrofit.Builder()
            .baseUrl("http://wikipedia.org")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WikiRestService::class.java)
}