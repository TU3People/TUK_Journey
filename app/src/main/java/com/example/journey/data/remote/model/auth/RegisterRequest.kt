package com.example.journey.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")      val username: String,
    @SerializedName("useremail")     val email: String,
    @SerializedName("userpassword")  val password: String
)
