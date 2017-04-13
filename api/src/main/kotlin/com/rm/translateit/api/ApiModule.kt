package com.rm.translateit.api

import com.rm.translateit.api.logger.Logger
import com.rm.translateit.api.translation.AllSources
import com.rm.translateit.api.translation.Sources
import com.rm.translateit.api.translation.source.Source
import dagger.Module
import dagger.Provides

@Module
internal class ApiModule (private val services: Set<Source>, private val logger: Logger) {

    @Provides
    fun allSources(): Sources = AllSources(services, logger)
}