package com.rm.translateit.api.translation.source.wiki

import com.rm.translateit.api.translation.source.wiki.response.LanguageLinksResult
import com.rm.translateit.api.translation.source.wiki.response.SuggestionResult
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Headers
import retrofit2.http.Query
import rx.Observable

internal interface WikiRestService {
    //TODO: also thinkg about client login https://www.mediawiki.org/wiki/API:Login

    @Headers("User-Agent: Android_Translate")
    @GET
    fun query(@Url url: String): Observable<LanguageLinksResult>

    @Headers("User-Agent: Android_Translate")
    @GET("/w/com.rm.translateit.api.php?action=query&format=json&prop=&list=search&titles=&srinfo=suggestion&srprop=redirecttitle")
    fun suggestions(@Query("srsearch") title: String, @Query("sroffset") offset: Int): Observable<SuggestionResult>
}