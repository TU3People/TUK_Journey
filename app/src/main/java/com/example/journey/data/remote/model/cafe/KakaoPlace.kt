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
    @SerializedName("address_name") val addressName: String,
    val x: String,
    val y: String,
    val phone: String,
    @SerializedName("place_url") val url: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    val distance: String,
    val thumbnailUrl: String? = null
) : Parcelable
