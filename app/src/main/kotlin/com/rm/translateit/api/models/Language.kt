package com.rm.translateit.api.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.modelAdapter
import com.raizlabs.android.dbflow.structure.Model
import com.rm.translateit.db.AppDatabase

@Table(database = AppDatabase::class)
data class Language(
        @PrimaryKey var code: String = "",
        @Column var name: String = "",
        @Column var originLastUsage: Long = 0,
        @Column var destinationLastUsage: Long = 0) : Model {
    override fun save() = modelAdapter<Language>().save(this)
    override fun delete() = modelAdapter<Language>().delete(this)
    override fun update() = modelAdapter<Language>().update(this)
    override fun insert() = modelAdapter<Language>().insert(this)
    override fun exists() = modelAdapter<Language>().exists(this)
}