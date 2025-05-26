package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.journey.data.remote.model.repo.ScheduleRepository
import com.example.journey.data.remote.model.timetable.SlotDto
import com.example.journey.databinding.FragmentNewScheduleBinding
import kotlinx.coroutines.launch

class NewScheduleFragment : Fragment() {

    private lateinit var binding: FragmentNewScheduleBinding
    private val repo by lazy { ScheduleRepository() }

    override fun onCreateView(i: LayoutInflater, p: ViewGroup?, s: Bundle?): View {
        binding = FragmentNewScheduleBinding.inflate(i, p, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) = with(binding) {
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val slots = makeSlotListFromUi()     // 직접 구현
            lifecycleScope.launch {
                val newId = repo.createSchedule(title, slots)
                Toast.makeText(context, "저장 완료(id=$newId)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeSlotListFromUi(): List<SlotDto> {
        // TimePicker 등으로 입력받은 값을 SlotDto 로 변환
        return listOf()
    }

    companion object { fun newInstance() = NewScheduleFragment() }
}
