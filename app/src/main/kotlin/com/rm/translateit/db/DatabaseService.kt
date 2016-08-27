package com.rm.translateit.db

import com.rm.translateit.db.model.LanguageModel

interface DatabaseService {
    fun languages(): List<LanguageModel>
}