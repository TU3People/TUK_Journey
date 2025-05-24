package com.example.journey.data.api

import com.example.journey.data.remote.network.model.cafe.NaverSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverSearchApi {
    @GET("v1/search/local.json")
    suspend fun searchCafe(
        @Query("query") query: String,
        @Query("display") display: Int = 10,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "random"
    ): Response<NaverSearchResponse>
}
