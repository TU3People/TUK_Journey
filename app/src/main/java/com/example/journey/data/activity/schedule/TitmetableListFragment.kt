package com.example.journey.data.activity.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.data.remote.Token
import com.example.journey.data.remote.model.timetable.ScheduleDto
import com.example.journey.data.remote.network.RetrofitProvider
import com.example.journey.databinding.FragmentTimetableListBinding
import com.example.journey.databinding.ItemTimetableBinding
import kotlinx.coroutines.launch

class TimetableListFragment : Fragment() {

    private var _binding: FragmentTimetableListBinding? = null
    private val binding get() = _binding!!

    // **간단한 in-memory 리스트**
    private val items = mutableListOf<ScheduleDto>()
    private val adapter by lazy { ScheduleAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TimetableListFragment.adapter
        }

        fetchSchedules()

    }

    private fun fetchSchedules() {

        lifecycleScope.launch {
            runCatching {
                RetrofitProvider.timetableApi.getAllSchedule() //Token.appContext.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("jwt_token", null)!!

            }.onSuccess { res ->
                if (res.isSuccessful) {
                    items.clear()
                    val schedules = res.body().orEmpty()      // ← 응답 리스트
                    items.addAll(schedules)
                    adapter.notifyDataSetChanged()

                    // ───── ① 첫 schedule_id를 SharedPreferences 에 저장 ─────
                    if (schedules.isNotEmpty()) {
                        val firstId = schedules[0].scheduleId     // ← 필드명 맞게 수정
                        Token.appContext
                            .getSharedPreferences("timetable", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sid", firstId.toString()) // 필요 시 putLong()
                            .apply()
                    }
                }
            }.onFailure { err ->
                err.printStackTrace() // TODO: 에러 메시지 처리
            }
        }
    }

    /* ───────────── Adapter & ViewHolder ───────────── */

    inner class ScheduleAdapter :
        RecyclerView.Adapter<ScheduleAdapter.VH>() {

        inner class VH(val vb: ItemTimetableBinding) :
            RecyclerView.ViewHolder(vb.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val vb = ItemTimetableBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return VH(vb)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val dto = items[position]

            holder.vb.tvTitle.text = dto.scheduleTitle

            // 간단 요약: 첫 슬롯 기준으로 “Mon 09:00~10:30 · 공유여부”
            val first = dto.scheduleData.slots.firstOrNull()
            val dayLabel = arrayOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
                .getOrElse(first?.day ?: 0) { "?" }
            val summary =
                "$dayLabel ${first?.start ?: ""}~${first?.end ?: ""} · " +
                        if (dto.isShared) "공유됨" else "개인"
            holder.vb.tvSummary.text = summary

            // 클릭 처리 (필요 시)
            holder.itemView.setOnClickListener {
                // TODO: 상세 시간표 화면으로 이동
            }
        }

        override fun getItemCount() = items.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
