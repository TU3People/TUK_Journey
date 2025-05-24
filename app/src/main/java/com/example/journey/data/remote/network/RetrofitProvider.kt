package com.example.journey.data.remote.network

import com.example.journey.data.remote.api.AuthApiService
import com.example.journey.data.remote.api.TimetableApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = "https://api.prayanne.co.kr"

    /* ─────── OkHttp ─────── */
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(TokenAuthInterceptor())     // JWT 헤더
            //.addInterceptor(HttpLoggingInterceptor().apply {
            //    level = HttpLoggingInterceptor.Level.BODY
            //})
            .build()
    }

    /* ─────── Retrofit ─────── */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /* ─────── API 서비스 팩토리 ─────── */
    /* 아래는 Interface를 기반으로 호출하게 되어있음 */
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val timetableApi: TimetableApiService by lazy {
        retrofit.create(TimetableApiService::class.java)
    }
}
