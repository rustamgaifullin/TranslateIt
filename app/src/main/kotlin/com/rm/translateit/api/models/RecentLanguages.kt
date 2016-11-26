package com.rm.translateit.api.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.modelAdapter
import com.raizlabs.android.dbflow.structure.Model
import com.rm.translateit.db.AppDatabase

@Table(database = AppDatabase::class)
data class RecentLanguages(
        @PrimaryKey(autoincrement = true) var id: Int = 0,
        @Column @ForeignKey var language: Language? = null,
        @Column var type: Int = 0,
        @Column var lastUse: Long = 0) : Model {
    override fun save() = modelAdapter<RecentLanguages>().save(this)
    override fun delete() = modelAdapter<RecentLanguages>().delete(this)
    override fun update() = modelAdapter<RecentLanguages>().update(this)
    override fun insert() = modelAdapter<RecentLanguages>().insert(this)
    override fun exists() = modelAdapter<RecentLanguages    >().exists(this)
}