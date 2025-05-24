package com.example.journey.remote.model.timetable

import com.google.gson.annotations.SerializedName

data class ScheduleDataDto(
    @SerializedName("slots")
    val slots: List<SlotDto>
)
