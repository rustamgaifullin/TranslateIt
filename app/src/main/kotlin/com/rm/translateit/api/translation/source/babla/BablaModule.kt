package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.translation.source.HtmlParser
import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
class BablaModule {

    @Provides
    @Singleton
    fun url() = BablaUrl()


    @Provides
    @Singleton
    fun parser(): HtmlParser = BablaHtmlParser()

    @Provides @IntoSet
    @Singleton
    fun service(url: BablaUrl, htmlParser: HtmlParser): Source = BablaSource(url, htmlParser)
}