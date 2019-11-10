package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
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
  fun service(
    url: WikiUrl,
    detailsUrl: WikiDetailsUrl,
    restService: WikiRestService
  ): Source = WikiSource(url, detailsUrl, restService)

  @Provides
  @Singleton
  fun restService(): WikiRestService = Retrofit.Builder()
      .baseUrl("http://wikipedia.org")
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build()
      .create(WikiRestService::class.java)
}