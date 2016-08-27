package com.rm.translateit.db.impl

import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.rm.translateit.db.DatabaseService
import com.rm.translateit.db.model.LanguageModel

class DatabaseServiceImpl: DatabaseService {
    override fun languages(): List<LanguageModel> {
        return (select from LanguageModel::class).list
    }

}