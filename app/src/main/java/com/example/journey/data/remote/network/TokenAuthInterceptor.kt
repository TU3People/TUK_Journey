package com.example.journey.data.remote.network

import android.content.Context
import com.example.journey.Token     // 기존에 있던 객체 그대로 사용
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder  = original.newBuilder()

        val token = Token.appContext
            .getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("jwt_token", null)

        token?.let { builder.addHeader("Authorization", "Bearer $it") }

        return chain.proceed(builder.build())
    }
}
