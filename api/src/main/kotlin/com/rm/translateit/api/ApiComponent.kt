package com.rm.translateit.api

import dagger.Component
import javax.inject.Singleton

@Component (modules = arrayOf(ApiModule::class))
@Singleton
internal interface ApiComponent {
    fun inject(api: Api)
}