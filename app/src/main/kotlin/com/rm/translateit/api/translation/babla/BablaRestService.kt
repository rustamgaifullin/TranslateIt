package com.rm.translateit.api.translation.babla

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface BablaRestService {

    @GET("dictionary/{from_to}/{word}")
    fun query(@Path("from_to") fromTo: String, @Path("word") word: String): Observable<ResponseBody>
}