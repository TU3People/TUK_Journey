package com.example.journey.remote.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")      val username: String,
    @SerializedName("useremail")     val email: String,
    @SerializedName("userpassword")  val password: String
)
