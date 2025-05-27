package com.example.journey.data.remote.api

import com.example.journey.data.remote.model.timetable.CreateScheduleRequest
import com.example.journey.data.remote.model.timetable.ScheduleDto
import com.example.journey.data.remote.model.timetable.SlotDto
import retrofit2.Response
import retrofit2.http.*

interface TimetableApiService {

    /* 현재 유저 올 조회 */
    @GET("/schedule")
    suspend fun getAllSchedule(
        @Path("id") id: String?
    ): Response<List<ScheduleDto>>

    /** 특정 시간표 조회 */
    @GET("/schedule/{id}")
    suspend fun getSchedule(
        @Path("id") id: Long
    ): Response<ScheduleDto>

    /** 새 시간표 생성 */
    @POST("/schedule")
    suspend fun createSchedule(
        @Body body: CreateScheduleRequest
    ): Response<ScheduleDto>

    /** 슬롯 추가/수정 */
    @PUT("/schedule/{id}/slots")
    suspend fun upsertSlots(
        @Path("id") id: Long,
        @Body slots: List<SlotDto>
    ): Response<Unit>
}
