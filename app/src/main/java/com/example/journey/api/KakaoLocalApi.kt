package com.example.journey.api

import com.example.journey.model.KakaoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun searchKeyword(
        @Query("query") query: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String,
        @Query("radius") radius: Int = 1000,
        @Query("sort") sort: String = "distance"
    ): Response<KakaoSearchResponse>
}
