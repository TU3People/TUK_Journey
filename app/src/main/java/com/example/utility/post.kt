package com.example.utility

import com.example.journey.Token
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val userpassword: String)
data class LoginResponse(val result: String, val message: String, val token: String)
data class ResisterRequest(val username: String, val useremail: String, val userpassword: String)
data class ResisterResponse(val result: String, val message: String)

interface ApiService {

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: ResisterRequest): Response<ResisterResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.prayanne.co.kr"

    // 토큰 가져오기
    private fun getToken(): String? {
        val pref = Token.appContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return pref.getString("jwt_token", null)
    }

    // OkHttpClient에 Interceptor 추가
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            // 아래의 코드가 이후, HTTP 요청이 있을 시, 토큰을 항상 포함하여 전송한다.
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                getToken()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }


    // Retrofit 인스턴스
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

