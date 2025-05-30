package com.example.journey.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result")      val result:    String,
    @SerializedName("message")     val message:   String,
    @SerializedName("token")       val token:     String,
    @SerializedName("id")          val id:        String,
    @SerializedName("username")    val username:  String,
    @SerializedName("useremail")   val useremail: String
)
