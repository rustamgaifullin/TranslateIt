package com.rm.translateit.api

import com.rm.translateit.api.models.Language

interface Translater {
    fun translate(word: String, from: String, to: String): String
    fun languages(): List<Language>
}