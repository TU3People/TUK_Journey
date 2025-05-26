package com.example.journey.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("username")      val username: String,
)
