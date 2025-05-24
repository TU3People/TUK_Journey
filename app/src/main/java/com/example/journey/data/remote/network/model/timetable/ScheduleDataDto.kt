package com.example.journey.data.remote.network.model.timetable

import com.google.gson.annotations.SerializedName

data class ScheduleDataDto(
    @SerializedName("slots")
    val slots: List<SlotDto>
)
