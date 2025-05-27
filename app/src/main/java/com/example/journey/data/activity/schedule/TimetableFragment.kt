package com.example.journey.ui.timetable

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.data.activity.schedule.Slot          // 화면용 모델:contentReference[oaicite:0]{index=0}
import com.example.journey.data.remote.Token
import com.example.journey.data.remote.model.timetable.SlotDto  // 네트워크 DTO:contentReference[oaicite:1]{index=1}
import com.example.journey.data.remote.network.RetrofitProvider // Retrofit 싱글턴:contentReference[oaicite:2]{index=2}
import com.example.journey.databinding.FragmentTimetableBinding
import com.example.journey.databinding.ItemSlotBinding
import kotlinx.coroutines.launch

class TimetableFragment : Fragment() {

    private var _vb: FragmentTimetableBinding? = null             // ViewBinding
    private val vb get() = _vb!!

    private val adapter = SlotAdapter()

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ) = FragmentTimetableBinding.inflate(i, c, false).also { _vb = it }.root

    override fun onViewCreated(v: View, s: Bundle?) {
        // 1) RecyclerView 세팅 ------------------------------------------------------
        vb.rvSlot.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = this@TimetableFragment.adapter
        }

        // 2) 서버에서 첫 시간표(id 전달 없으면 1로 가정) -------------------------------
//        val schedId = arguments?.getLong("schedule_id") ?: 1L
        val pref = Token.appContext
            .getSharedPreferences("timetable", Context.MODE_PRIVATE)
        val schedId = pref.getString("sid", null)?.toLong()!!
        lifecycleScope.launch {
            kotlin.runCatching {
                RetrofitProvider.timetableApi.getSchedule(schedId)   // suspend
            }.onSuccess { res ->
                if (res.isSuccessful) {
                    val slots = res.body()
                        ?.scheduleData
                        ?.slots
                        ?.map { it.toDomain() } ?: emptyList()
                    adapter.submitList(slots)
                }
            }.onFailure { it.printStackTrace() /* TODO: 에러 처리 */ }
        }
    }

    override fun onDestroyView() { _vb = null; super.onDestroyView() }

    /* ────────────────────── SlotDto → Slot 변환 ────────────────────── */
    private fun SlotDto.toDomain() = Slot(
        day,
        start.toMin(),
        end.toMin(),
        subject,
        Color.parseColor(color)
    )

    private fun String.toMin(): Int =
        split(":").let { (h, m) -> h.toInt() * 60 + m.toInt() }

    /* ─────────────────────── Adapter & DiffUtil ────────────────────── */
    private inner class SlotAdapter :
        ListAdapter<Slot, SlotAdapter.VH>(Diff) {

        inner class VH(val b: ItemSlotBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(p: ViewGroup, vt: Int) = VH(
            ItemSlotBinding.inflate(LayoutInflater.from(p.context), p, false)
        )

        override fun onBindViewHolder(h: VH, pos: Int) = with(h.b) {
            val s = getItem(pos)
            tvSubject.text = s.subject
            tvTime.text =
                "%02d:%02d-%02d:%02d".format(s.startMin/60, s.startMin%60,
                    s.endMin/60,   s.endMin%60)
            card.setCardBackgroundColor(s.color)
        }
    }
    private object Diff : DiffUtil.ItemCallback<Slot>() {
        override fun areItemsTheSame(o: Slot, n: Slot) = o === n
        override fun areContentsTheSame(o: Slot, n: Slot) = o == n
    }
}
