package com.rm.translateit

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.rm.translateit.api.MainComponent

class TranslateApplication : Application() {
    private lateinit var translationComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this)
                .build())

//        val providers = DaggerSourcesComponent.create().sources()
//        translationComponent = DaggerMainComponent.builder().mainModule(MainModule(providers)).build()
    }

    fun translationComponent(): MainComponent = translationComponent
}