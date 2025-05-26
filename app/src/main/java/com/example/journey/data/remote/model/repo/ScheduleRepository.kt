package com.example.journey.data.remote.model.repo

import android.graphics.Color
import com.example.journey.data.activity.schedule.Slot
import com.example.journey.data.activity.schedule.toMinutes
import com.example.journey.data.remote.api.TimetableApiService
import com.example.journey.data.remote.model.timetable.CreateScheduleRequest
import com.example.journey.data.remote.model.timetable.ScheduleDataDto
import com.example.journey.data.remote.model.timetable.SlotDto
import com.example.journey.data.remote.network.RetrofitProvider

class ScheduleRepository(
    private val api: TimetableApiService = RetrofitProvider.timetableApi
) {
    suspend fun getSchedule(id: Long) = api.getSchedule(id).body()
    suspend fun getAllSchedules()  = api.getAllSchedules().body() ?: emptyList()
    suspend fun createSchedule(title: String, slots: List<SlotDto>): Long? {
        val dto = api.createSchedule(CreateScheduleRequest(username = "", title,
            ScheduleDataDto(slots))).body()
        return dto?.scheduleId
    }
    suspend fun upsertSlots(id: Long, slots: List<SlotDto>) =
        api.upsertSlots(id, slots)

    suspend fun deleteSchedule(id: Long) =
        api.deleteSchedule(id)                     // DELETE 엔드포인트 필요
}

/* SlotDto → Slot (화면용) */
fun SlotDto.toDomain(): Slot = Slot(
    day,
    start.toMinutes(),
    end.toMinutes(),
    subject,
    Color.parseColor(color)
)

/* Slot → SlotDto (서버 저장용) */
fun Slot.toDto(): SlotDto = SlotDto(
    day,           // 0=Sun…
    "%02d:%02d".format(startMin/60, startMin%60),
    "%02d:%02d".format(endMin/60,   endMin%60),
    subject,
    location = "", // UI 확장 예정
    color = String.format("#%06X", 0xFFFFFF and color)
)