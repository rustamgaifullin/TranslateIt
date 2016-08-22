package com.rm.translateit.api

import com.rm.translateit.api.mock.FakeTranslater

class TranslaterContext {
    companion object {
        private val context = FakeTranslater()

        fun getContext(): Translater {
            return context;
        }
    }
}