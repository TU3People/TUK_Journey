package com.example.journey.data.remote.network.model.timetable

import com.google.gson.annotations.SerializedName

data class ScheduleDto(
    @SerializedName("schedule_id")    val scheduleId: Long,
    @SerializedName("schedule_title") val scheduleTitle: String,
    @SerializedName("schedule_data")  val scheduleData: ScheduleDataDto,
    @SerializedName("is_shared")      val isShared: Boolean
)
