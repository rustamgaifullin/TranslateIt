package com.rm.translateit.api.translation

import com.rm.translateit.api.translation.source.Source
import com.rm.translateit.api.translation.source.babla.BablaModule
import com.rm.translateit.api.translation.source.wiki.WikiModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = arrayOf(
        WikiModule::class,
        BablaModule::class
    )
)
@Singleton
internal interface SourcesComponent {
  fun sources(): Set<Source>
}