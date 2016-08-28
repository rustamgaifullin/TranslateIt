package com.rm.translateit.api.wiki

import com.rm.translateit.api.wiki.response.LanguageLinksResult
import com.rm.translateit.api.wiki.response.SuggestionResult
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface WikiRestService {
    @GET("w/api.php?action=query&prop=langlinks&format=json&lllimit=500")
    fun query(@Query("titles") title: String): Observable<LanguageLinksResult>

    @GET("/w/api.php?action=query&format=json&prop=&list=search&titles=&srinfo=suggestion&srprop=redirecttitle")
    fun suggestions(@Query("srsearch") title: String, @Query("sroffset") offset: Int): Observable<SuggestionResult>
}