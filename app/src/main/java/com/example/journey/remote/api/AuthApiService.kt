package com.example.journey.remote.api

import com.example.data.remote.model.auth.*
import com.example.journey.remote.model.auth.LoginRequest
import com.example.journey.remote.model.auth.LoginResponse
import com.example.journey.remote.model.auth.RegisterRequest
import com.example.journey.remote.model.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/login")
    suspend fun login(
        @Body req: LoginRequest
    ): Response<LoginResponse>

    @POST("/register")
    suspend fun register(
        @Body req: RegisterRequest
    ): Response<RegisterResponse>
}
