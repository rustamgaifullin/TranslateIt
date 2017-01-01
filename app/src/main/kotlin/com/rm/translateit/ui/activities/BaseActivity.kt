package com.rm.translateit.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import rx.subscriptions.CompositeSubscription

abstract class BaseActivity : AppCompatActivity() {
    protected val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        prepareUI()
    }

    override fun onResume() {
        super.onResume()
        createBindings()
    }

    override fun onPause() {
        super.onPause()
        subscriptions.unsubscribe()
    }

    abstract fun getLayoutId(): Int

    abstract fun prepareUI()

    abstract fun createBindings()
}