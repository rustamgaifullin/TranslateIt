package com.rm.translateit.api

import com.rm.translateit.api.languages.DBLanguages
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.translation.Sources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {
    @Provides
    @Singleton
    fun languages(): Languages = DBLanguages()

    @Provides
    @Singleton
    fun allSources(): Sources = Api().sources
}