package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
class WikiModule {

    @Provides
    @Singleton
    fun url() = WikiUrl()

    @Provides @IntoSet
    @Singleton
    fun service(url: WikiUrl): Source = WikiSource(url)
}