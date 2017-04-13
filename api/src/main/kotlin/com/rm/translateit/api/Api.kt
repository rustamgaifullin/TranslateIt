package com.rm.translateit.api

import com.rm.translateit.api.logger.EmptyLogger
import com.rm.translateit.api.logger.Logger
import com.rm.translateit.api.translation.DaggerSourcesComponent
import com.rm.translateit.api.translation.Sources
import javax.inject.Inject

class Api(private val logger: Logger = EmptyLogger()) {

    @Inject
    lateinit internal var sources: Sources

    init {
        val sources = DaggerSourcesComponent
                .create()
                .sources()
        val apiModule = ApiModule(sources, logger)

        DaggerApiComponent.builder()
                .apiModule(apiModule)
                .build()
                .inject(this)
    }

    fun allSources() = sources
}