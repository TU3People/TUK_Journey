package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journey.data.remote.model.repo.ScheduleRepository
import com.example.journey.data.remote.model.timetable.ScheduleDto
import com.example.journey.databinding.ActivityScheduleBinding
import com.example.journey.databinding.FragmentScheduleListBinding
import kotlinx.coroutines.launch

class ScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentScheduleListBinding
    private val adapter = ScheduleAdapter(::onItemClick, ::onItemLongClick)
    private val repo by lazy { ScheduleRepository() }

    override fun onCreateView(i: LayoutInflater, p: ViewGroup?, s: Bundle?): View {
        binding = FragmentScheduleListBinding.inflate(i, p, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            adapter.submitList(repo.getAllSchedules())   // GET /schedules
        }
    }

    private fun onItemClick(item: ScheduleDto) {
        // ‘시간표’ 탭으로 이동 & TimetableFragment 에 id 전달
        (activity as? ScheduleActivity)?.openTimetableTab()   // 외부 접근으로 해결
        parentFragmentManager.setFragmentResult(
            "openSchedule",
            bundleOf("scheduleId" to item.scheduleId)
        )
    }

    private fun onItemLongClick(item: ScheduleDto): Boolean {
        // 예: 삭제 메뉴
        PopupMenu(requireContext(), binding.recyclerView).apply {
            menu.add("삭제").setOnMenuItemClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.deleteSchedule(item.scheduleId)
                    adapter.submitList(repo.getAllSchedules()) // 새로고침
                }
                true
            }
        }.show()
        return true
    }

    companion object { fun newInstance() = ScheduleListFragment() }
}