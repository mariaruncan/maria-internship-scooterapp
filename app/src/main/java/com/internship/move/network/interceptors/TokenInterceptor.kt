package com.internship.move.network.interceptors

import com.internship.move.utils.InternalStorageManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val internalStorageManager: InternalStorageManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url().url().path
        val newRequest = chain.request().newBuilder()
        if (path != LOGIN_PATH && path != REGISTER_PATH) {
            newRequest.addHeader("Authorization", "Bearer ${internalStorageManager.getToken()}").build()
        }
        return chain.proceed(newRequest.build())
    }

    companion object {
        private const val LOGIN_PATH = "/users/login"
        private const val REGISTER_PATH = "/users/register"
    }
}