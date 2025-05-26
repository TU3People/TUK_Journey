package com.example.journey.data.remote.api

import com.example.journey.data.remote.model.user.ProfileRequest
import com.example.journey.data.remote.model.user.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/profile")
    suspend fun profile(
        @Body req: ProfileRequest
    ): Response<ProfileResponse>

}