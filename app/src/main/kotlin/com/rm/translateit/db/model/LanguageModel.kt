package com.rm.translateit.db.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.rm.translateit.db.AppDatabase

@Table(database = AppDatabase::class)
class LanguageModel: BaseModel() {
    @PrimaryKey var code: String = ""
    @Column var name: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as LanguageModel

        if (code != other.code) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "LanguageModel(code='$code', name='$name')"
    }
}
