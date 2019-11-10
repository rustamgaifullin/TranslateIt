package com.rm.translateit

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.rm.translateit.api.DaggerMainComponent
import com.rm.translateit.api.MainComponent

class TranslateApplication : Application() {
  private lateinit var translationComponent: MainComponent

  override fun onCreate() {
    super.onCreate()
    FlowManager.init(
        FlowConfig.Builder(this)
            .build()
    )

    translationComponent = DaggerMainComponent.create()
  }

  fun translationComponent(): MainComponent = translationComponent
}