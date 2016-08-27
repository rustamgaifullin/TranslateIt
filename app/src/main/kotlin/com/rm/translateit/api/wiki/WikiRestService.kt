package com.rm.translateit.api.wiki

import com.rm.translateit.api.wiki.response.LanguageLinksResult
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface WikiRestService {
    @GET("w/api.php?action=query&prop=langlinks&format=json&lllimit=500")
    fun query(@Query("titles") title: String): Observable<LanguageLinksResult>
}