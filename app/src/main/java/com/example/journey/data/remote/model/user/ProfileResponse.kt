package com.example.journey.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id")            val id:        String,
    @SerializedName("username")      val username:  String,
    @SerializedName("useremail")     val useremail: String
)