package com.rm.translateit.db

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
  companion object {
    const val NAME = "storage"
    const val VERSION = 2
  }
}