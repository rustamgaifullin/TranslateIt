package com.rm.translateit.api

import com.rm.translateit.api.languages.DBLanguages
import com.rm.translateit.api.languages.Languages
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {
    @Provides
    @Singleton
    fun languages(): Languages = DBLanguages()
}