package com.rm.translateit.api.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ManyToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.innerJoin
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import com.rm.translateit.db.AppDatabase

@Table(database = AppDatabase::class)
@ManyToMany(referencedTable = Name::class)
data class Language(
  @PrimaryKey var code: String = "",
  @Column var originLastUsage: Long = 0,
  @Column var destinationLastUsage: Long = 0,
  @Column var dictionary: String = ""
) : BaseModel() {

  companion object {
    private const val DEFAULT_LANGUAGE_CODE = "en"
  }

  var names: List<Name> = listOf()
    get() = SQLite
        .select()
        .from(Name::class)
        .innerJoin(Language_Name::class)
        .on(Name_Table.id.eq(Language_Name_Table._id))
        .where(Language_Name_Table.language_code.eq(code))
        .queryList()

  fun contains(code: String) = SQLite
      .selectCountOf(Name_Table.id)
      .from(Name::class)
      .where(Name_Table.code.eq(code))
      .longValue() > 0

  fun findName(localeCode: String): String {
    var codeToSearch = DEFAULT_LANGUAGE_CODE

    if (contains(localeCode)) {
      codeToSearch = localeCode
    }

    return SQLite
        .select()
        .from(Name::class)
        .innerJoin(Language_Name::class)
        .on(Name_Table.id.eq(Language_Name_Table._id))
        .where(Language_Name_Table.language_code.eq(codeToSearch))
        .and(Name_Table.code.eq(code))
        .querySingle()!!.name
  }
}