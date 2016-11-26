package com.rm.translateit.api.languages

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.Language_Table
import com.rm.translateit.api.models.RecentLanguages
import com.rm.translateit.api.models.RecentLanguages_Table

class DBLanguages : Languages {
    override fun languages(): List<Language> {
        return (select from Language::class).list
    }

    override fun originLanguages(): List<Language> {
        return SQLite.select()
                .from(Language::class)
                .leftOuterJoin(RecentLanguages::class.java).on(Language_Table.code.withTable().eq(RecentLanguages_Table.language_code.withTable()))
                .queryList()
    }

    override fun destinationLanguages(exceptOrigin: String): List<Language> {
        val exceptOriginToLower = exceptOrigin.toLowerCase()

        return SQLite.select()
                .from(Language::class)
                .leftOuterJoin(RecentLanguages::class.java).on(Language_Table.code.withTable().eq(RecentLanguages_Table.language_code.withTable()))
                .where(Language_Table.code.notEq(exceptOriginToLower))
                .queryList()
    }

    override fun updateLastUsage(model: Language) {
    }
}