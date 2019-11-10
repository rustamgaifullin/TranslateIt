package com.rm.translateit.api.translation.source.babla

import okhttp3.Interceptor
import okhttp3.Response

internal class BablaInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalResponse: Response = chain.proceed(chain.request())
    var newResponse = originalResponse
    if (originalResponse.code == 301) {
      newResponse = originalResponse.newBuilder()
          .code(200)
          .build()
    }

    return newResponse
  }
}