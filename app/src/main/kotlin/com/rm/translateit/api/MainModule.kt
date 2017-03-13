package com.rm.translateit.api

import com.rm.translateit.api.languages.DBLanguages
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.translation.AllSources
import com.rm.translateit.api.translation.Sources
import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(private val sources: Set<Source>) {
    @Provides
    @Singleton
    fun sources(): Sources = AllSources(sources)

    @Provides
    @Singleton
    fun languages(): Languages = DBLanguages()
}