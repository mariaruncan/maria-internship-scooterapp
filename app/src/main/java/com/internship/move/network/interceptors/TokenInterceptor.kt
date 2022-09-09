package com.internship.move.network.interceptors

import com.internship.move.utils.InternalStorageManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val internalStorageManager: InternalStorageManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = internalStorageManager.getToken()
        return if (token != null) {
            chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build())
        } else {
            chain.proceed(chain.request())
        }
    }
}