package com.rm.translateit.api

import com.rm.translateit.ui.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(MainModule::class))
interface MainComponent {
  fun inject(mainActivity: MainActivity)
}