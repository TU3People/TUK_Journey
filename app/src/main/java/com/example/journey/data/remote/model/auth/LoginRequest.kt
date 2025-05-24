package com.example.journey.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")      val username: String,
    @SerializedName("userpassword")  val password: String
)
