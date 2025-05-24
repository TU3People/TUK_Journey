package com.example.journey.data.remote.model.timetable

import com.google.gson.annotations.SerializedName

data class ScheduleDataDto(
    @SerializedName("slots")
    val slots: List<SlotDto>
)
