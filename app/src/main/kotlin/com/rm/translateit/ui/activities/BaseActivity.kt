package com.rm.translateit.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import rx.subscriptions.CompositeSubscription

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var subscriptions: CompositeSubscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        prepareUI()
    }

    override fun onStart() {
        super.onStart()

        subscriptions = CompositeSubscription()
    }

    override fun onResume() {
        d("BaseActivity", "onResume called")
        super.onResume()
        createBindings()
    }

    override fun onPause() {
        d("BaseActivity", "onPause called")
        super.onPause()
        subscriptions.clear()
    }

    override fun onStop() {
        super.onStop()

        subscriptions.unsubscribe()
    }

    abstract fun getLayoutId(): Int

    abstract fun prepareUI()

    abstract fun createBindings()
}