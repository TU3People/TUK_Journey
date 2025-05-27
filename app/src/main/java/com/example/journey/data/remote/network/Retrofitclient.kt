package com.example.journey.data.remote.network

import android.util.Log
import com.example.journey.data.remote.api.KakaoLocalApi
import com.example.journey.data.remote.api.NaverSearchApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 네이버 오픈 API (지역 검색)
    val openApi: NaverSearchApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("X-Naver-Client-Id", "6KTKp2ACYid5gqWS9X7f")
                            .addHeader("X-Naver-Client-Secret", "XKCjKlJZHG")
                            .build()
                        Log.d("OpenAPI", "➡️ 요청 URL: ${request.url}")
                        chain.proceed(request)
                    }
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverSearchApi::class.java)
    }

    // 카카오 로컬 API (장소 검색용)
    private const val KAKAO_API_KEY = "KakaoAK c6c94dffba4a484a8a9121497aac09d0"

    val kakaoApi: KakaoLocalApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", KAKAO_API_KEY)
                            .build()
                        Log.d("KakaoAPI", "➡️ 요청 URL: ${request.url}")
                        chain.proceed(request)
                    }
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalApi::class.java)
    }
}
