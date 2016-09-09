package com.rm.translateit.api.wiki

import com.rm.translateit.api.wiki.response.LanguageLinksResult
import com.rm.translateit.api.wiki.response.SuggestionResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rx.Observable

interface WikiRestService {
    //TODO: also thinkg about client login https://www.mediawiki.org/wiki/API:Login

    @Headers("User-Agent: Android_Translate")
    @GET("w/api.php?action=query&prop=langlinks&format=json&lllimit=500&redirects")
    fun query(@Query("titles") title: String, @Query("lllang") to: String): Observable<LanguageLinksResult>

    @Headers("User-Agent: Android_Translate")
    @GET("/w/api.php?action=query&format=json&prop=&list=search&titles=&srinfo=suggestion&srprop=redirecttitle")
    fun suggestions(@Query("srsearch") title: String, @Query("sroffset") offset: Int): Observable<SuggestionResult>
}