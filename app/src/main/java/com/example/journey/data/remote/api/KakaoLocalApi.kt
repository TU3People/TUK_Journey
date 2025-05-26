package com.example.journey.data.remote.api

import com.example.journey.data.remote.model.cafe.KakaoSearchResponse
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
        @Query("sort") sort: String = "accuracy"
    ): Response<KakaoSearchResponse>
}
