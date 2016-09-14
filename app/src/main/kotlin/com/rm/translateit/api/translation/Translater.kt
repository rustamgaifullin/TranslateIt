package com.rm.translateit.api.translation

import rx.Observable

interface Translater {
    fun translate(word: String, from: String, to: String): Observable<String>
    fun suggestions(title: String, from: String, offset: Int): Observable<List<String>>
}