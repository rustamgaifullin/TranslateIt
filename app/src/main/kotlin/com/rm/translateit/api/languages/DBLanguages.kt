package com.rm.translateit.api.languages

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.LanguageModel_Table

class DBLanguages : Languages {
    override fun all(): List<LanguageModel> {
        return (select from LanguageModel::class).list
    }

    override fun originLanguages(): List<LanguageModel> {
        return SQLite.select()
                .from(LanguageModel::class)
                .orderBy(LanguageModel_Table.originLastUsage.nameAlias, false)
                .queryList()
    }

    override fun destinationLanguages(exceptOriginCode: String): List<LanguageModel> {
        val exceptOriginToLower = exceptOriginCode.toLowerCase()

        return SQLite.select()
                .from(LanguageModel::class)
                .where(LanguageModel_Table.code.notEq(exceptOriginToLower))
                .orderBy(LanguageModel_Table.destinationLastUsage.nameAlias, false)
                .queryList()
    }

    override fun updateOriginLastUsage(model: LanguageModel) {
        model.originLastUsage = System.currentTimeMillis()
        model.save()
    }

    override fun updateDestinationLastUsage(model: LanguageModel) {
        model.destinationLastUsage = System.currentTimeMillis()
        model.save()
    }
}