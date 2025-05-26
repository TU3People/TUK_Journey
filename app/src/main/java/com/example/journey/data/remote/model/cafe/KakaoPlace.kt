package com.example.journey.data.remote.model.cafe

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class KakaoSearchResponse(
    @SerializedName("documents")
    val documents: List<KakaoPlace>
)

@Parcelize
data class KakaoPlace(
    @SerializedName("place_name") val name: String,
    @SerializedName("road_address_name") val roadAddress: String,
    val x: String, // 경도
    val y: String, // 위도
    val phone: String,
    @SerializedName("place_url") val url: String,
    val thumbnailUrl: String? = null
) : Parcelable