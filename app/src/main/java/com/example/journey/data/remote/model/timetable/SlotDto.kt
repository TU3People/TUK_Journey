package com.example.journey.data.remote.model.timetable

import com.google.gson.annotations.SerializedName

data class SlotDto(
    @SerializedName("day")       val day: Int,     // 0=Sun,1=Mon…
    @SerializedName("start")     val start: String, // "09:00"
    @SerializedName("end")       val end: String,   // "10:30"
    @SerializedName("subject")   val subject: String,
    @SerializedName("location")  val location: String,
    @SerializedName("color")     val color: String
)
