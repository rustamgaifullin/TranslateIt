package com.rm.translateit.api

import com.rm.translateit.api.mock.FakeTranslater
import com.rm.translateit.api.wiki.WikiTranslater
import com.rm.translateit.db.DatabaseService
import com.rm.translateit.db.impl.DatabaseServiceImpl

class TranslaterContext {
    companion object {
        private val context = WikiTranslater()
        private val db = DatabaseServiceImpl()

        fun getContext(): Translater {
            return context
        }

        fun getDB(): DatabaseService {
            return db
        }
    }
}