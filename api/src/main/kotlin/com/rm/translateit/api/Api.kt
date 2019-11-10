package com.rm.translateit.api

import com.rm.translateit.api.logger.EmptyLogger
import com.rm.translateit.api.logger.Logger
import com.rm.translateit.api.translation.AllSources
import com.rm.translateit.api.translation.DaggerSourcesComponent
import com.rm.translateit.api.translation.Sources

class Api(private val logger: Logger = EmptyLogger()) {

  val sources: Sources

  init {
    val sourceSet = DaggerSourcesComponent
        .create()
        .sources()

    sources = AllSources(sourceSet, logger)
  }
}