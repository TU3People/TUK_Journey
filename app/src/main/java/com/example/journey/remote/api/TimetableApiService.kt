package com.example.journey.remote.api

import com.example.data.remote.model.timetable.*
import com.example.journey.remote.model.timetable.CreateScheduleRequest
import com.example.journey.remote.model.timetable.ScheduleDto
import com.example.journey.remote.model.timetable.SlotDto
import retrofit2.Response
import retrofit2.http.*

interface TimetableApiService {

    /** 특정 시간표 조회 */
    @GET("/schedules/{id}")
    suspend fun getSchedule(
        @Path("id") id: Long
    ): Response<ScheduleDto>

    /** 새 시간표 생성 */
    @POST("/schedules")
    suspend fun createSchedule(
        @Body body: CreateScheduleRequest
    ): Response<ScheduleDto>

    /** 슬롯 추가/수정 */
    @PUT("/schedules/{id}/slots")
    suspend fun upsertSlots(
        @Path("id") id: Long,
        @Body slots: List<SlotDto>
    ): Response<Unit>
}
