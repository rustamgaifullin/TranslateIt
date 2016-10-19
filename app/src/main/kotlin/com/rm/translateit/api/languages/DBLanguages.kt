package com.rm.translateit.api.languages

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.rm.translateit.api.models.Language

//TODO: how to test?
class DBLanguages : Languages{
    override fun languages(): List<Language> {
        return (select from Language::class).list
    }
}