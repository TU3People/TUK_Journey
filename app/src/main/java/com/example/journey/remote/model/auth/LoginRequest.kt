package com.example.journey.remote.model.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")      val username: String,
    @SerializedName("userpassword")  val password: String
)
