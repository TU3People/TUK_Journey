package com.example.journey.data.remote.model.cafe

import com.google.gson.annotations.SerializedName

data class KakaoSearchResponse(
    val documents: List<KakaoPlace>
)

data class KakaoPlace(
    @SerializedName("place_name") val name: String,
    @SerializedName("road_address_name") val roadAddress: String,
    val x: String, // 경도
    val y: String, // 위도
    val phone: String,
    @SerializedName("place_url") val url: String,

    val thumbnailUrl: String? = null
)

