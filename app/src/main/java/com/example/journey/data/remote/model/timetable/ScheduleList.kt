
import com.example.journey.data.remote.model.timetable.ScheduleDataDto
import com.google.gson.annotations.SerializedName

data class ScheduleList(
    @SerializedName("slots")
    val slots: List<ScheduleDataDto>
)
