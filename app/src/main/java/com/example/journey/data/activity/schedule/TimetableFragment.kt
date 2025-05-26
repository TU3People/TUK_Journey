package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.journey.data.activity.schedule.model.TimetableViewModel
import com.example.journey.databinding.FragmentTimetableBinding

//@AndroidEntryPoint   // Hilt 사용 시
class TimetableFragment : Fragment() {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimetableViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = FragmentTimetableBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        viewModel.slots.observe(viewLifecycleOwner) { slotList ->
            binding.gridTimetable.submitSlots(slotList)
        }

        // 예시: 첫 화면에 id=1 시간표를 보여준다
        viewModel.loadSchedule(1L)
    }

    companion object { fun newInstance() = TimetableFragment() }
}