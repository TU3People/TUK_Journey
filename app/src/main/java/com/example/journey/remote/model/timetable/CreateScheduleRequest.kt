package com.example.journey.remote.model.timetable

import com.google.gson.annotations.SerializedName

data class CreateScheduleRequest(
    @SerializedName("username")       val username: String,
    @SerializedName("schedule_title") val title: String,
    @SerializedName("schedule_data")  val data: ScheduleDataDto
)
