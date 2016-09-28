package com.rm.translateit.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        prepareUI()
        createBindings()
    }

    abstract fun getLayoutId(): Int

    abstract fun prepareUI()

    abstract fun createBindings()
}