package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.journey.data.remote.model.timetable.CreateScheduleRequest
import com.example.journey.data.remote.model.timetable.ScheduleDataDto
import com.example.journey.data.remote.model.timetable.SlotDto
import com.example.journey.data.remote.network.RetrofitProvider
import com.example.journey.databinding.FragmentTimetableNewBinding
import kotlinx.coroutines.launch

class TimetableNewFragment : Fragment() {

    private var _binding: FragmentTimetableNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableNewBinding.inflate(inflater, container, false)
        // ‘저장’ 버튼 클릭 리스너 등 설정

        _binding!!.btntest.setOnClickListener{uploadSchedule()}
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun uploadSchedule() {
        /* 1) 슬롯 리스트 만들기 ─ 실제 화면 데이터 → DTO */
        val slots = listOf(
            SlotDto(
                day = 1, start = "09:00", end = "10:30",
                subject = "OS", location = "", color = "#FF7043"
            ),
            SlotDto(2, "13:30", "15:00", "Math", "", "#4CAF50")
        )

        /* 2) 최상위 Request 작성 */
        val body = CreateScheduleRequest(
            username = "1",
            title = "1 여행 시간표",
            data = ScheduleDataDto(slots)          // 내부 JSON 객체
        )

        /* 3) 코루틴으로 전송 */
        lifecycleScope.launch {
            try {
                val res = RetrofitProvider.timetableApi
                    .createSchedule(body)          // POST /schedules
                if (res.isSuccessful) {
                    Toast.makeText(requireContext(),
                        "저장 완료! id=${res.body()!!.scheduleId}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),
                        "서버 오류 ${res.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(),
                    "네트워크 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}



