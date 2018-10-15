package com.rm.translateit.api.models

import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.rm.translateit.db.AppDatabase

@Table(database = AppDatabase::class)
data class Name(
        @PrimaryKey var id: Long = 0,
        var code: String = "",
        var name: String = "") : BaseModel()