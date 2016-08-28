package com.rm.translateit.api

import com.rm.translateit.api.models.Language
import rx.Observable

interface Translater {
    fun translate(word: String, from: String, to: String): Observable<String>
    fun languages(): List<Language>
    fun suggestions(title: String, from: String, offset: Int): Observable<List<String>>
}