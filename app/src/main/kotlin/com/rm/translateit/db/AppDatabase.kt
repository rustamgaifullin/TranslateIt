package com.rm.translateit.db

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, foreignKeysSupported = true)
class AppDatabase {
    companion object {
        const val NAME = "DB"
        const val VERSION = 2
    }
}