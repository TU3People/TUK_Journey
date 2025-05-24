package com.example.journey.data.remote.network.model.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result")  val result: String,
    @SerializedName("message") val message: String,
    @SerializedName("token")   val token: String
)
