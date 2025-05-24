package com.example.journey.data.remote.network.api

import com.example.journey.data.remote.network.model.auth.*
import com.example.journey.data.remote.network.model.auth.LoginRequest
import com.example.journey.data.remote.network.model.auth.LoginResponse
import com.example.journey.data.remote.network.model.auth.RegisterRequest
import com.example.journey.data.remote.network.model.auth.RegisterResponse
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
