package com.rm.translateit.api.translation.source.babla

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

internal interface BablaRestService {

    @GET
    fun translate(@Url url: String): Observable<ResponseBody>
}