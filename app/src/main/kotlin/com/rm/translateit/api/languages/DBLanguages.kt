package com.rm.translateit.api.languages

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.Language_Table

class DBLanguages : Languages {
    override fun languages(): List<Language> {
        return (select from Language::class).list
    }

    override fun originLanguages(): List<Language> {
        return SQLite.select()
                .from(Language::class)
                .orderBy(Language_Table.originLastUsage.nameAlias, true)
                .queryList()
    }

    override fun destinationLanguages(exceptOriginCode: String): List<Language> {
        val exceptOriginToLower = exceptOriginCode.toLowerCase()

        return SQLite.select()
                .from(Language::class)
                .where(Language_Table.code.notEq(exceptOriginToLower))
                .orderBy(Language_Table.destinationLastUsage.nameAlias, true)
                .queryList()
    }

    override fun updateOriginLastUsage(model: Language) {
        model.originLastUsage = System.currentTimeMillis()
        model.save()
    }

    override fun updateDestinationLastUsage(model: Language) {
        model.destinationLastUsage = System.currentTimeMillis()
        model.save()
    }
}