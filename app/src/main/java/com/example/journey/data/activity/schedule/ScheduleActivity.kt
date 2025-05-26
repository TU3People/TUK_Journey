package com.example.journey.data.activity.schedule

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.data.activity.schedule.Slot
import com.example.journey.data.activity.schedule.toMinutes
import com.example.journey.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.data.remote.model.timetable.ScheduleDto
import com.example.journey.databinding.ItemScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    val titles = arrayListOf("시간표", "시간표 리스트", "새 시간표")

    // 외부 접근을 허용
    fun openTimetableTab() {
        binding.viewPager.currentItem = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabFunc()

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = titles.size
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> TimetableFragment.newInstance()          // 기본(첫) 시간표
                1 -> ScheduleListFragment.newInstance()
                else -> NewScheduleFragment.newInstance()
            }
        }

        // ── TabLayout ↔ ViewPager2 연결 ──
        TabLayoutMediator(binding.schTab, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()

        /*
        /* 1) 서버 호출 → JSON → Slot 리스트 변환
        //Slot(day, start, end, subject, Color)
              여기서는 하드코딩 예시 */
        val slots = listOf(
            Slot(1, "09:00".toMinutes(), "10:30".toMinutes(), "OS",   Color.parseColor("#FF7043")),
            Slot(2, "13:30".toMinutes(), "15:00".toMinutes(), "Math", Color.parseColor("#4CAF50"))

        )

        /* 2) Custom View(GridTimetable)에 리스트 투입 */
        binding.gridTimetable.submitSlots(slots)
        */
    }

    private fun tabFunc(){

        for(i in 0..titles.size - 1){ binding.schTab.addTab(binding.schTab.newTab().apply { text = titles[i] })}

        binding.schTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Toast.makeText(applicationContext, "d", Toast.LENGTH_LONG).show()
//                when(tab?.text){
//                    list1[0] ->
//                }
            }
        })
    }
}

/**
 * 시간표 목록 전용 RecyclerView 어댑터
 *
 * @param click   항목 탭(single click) 콜백
 * @param longClick  항목 롱프레스 콜백 (true 반환 시 컨텍스트 메뉴 표시 등)
 */
class ScheduleAdapter(
    private val click: (ScheduleDto) -> Unit,
    private val longClick: (ScheduleDto) -> Boolean
) : ListAdapter<ScheduleDto, ScheduleAdapter.VH>(Diff) {

    /** 뷰 홀더 */
    inner class VH(private val bind: ItemScheduleBinding) :
        RecyclerView.ViewHolder(bind.root) {

        fun bind(item: ScheduleDto) = with(bind) {
            tvTitle.text   = item.scheduleTitle
//            tvUpdated.text = item.updatedAt.toLocalDateTimeString()

            root.setOnClickListener  { click(item) }
            root.setOnLongClickListener { longClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    /** DiffUtil 로 두 리스트 차이 계산 */
    private object Diff : DiffUtil.ItemCallback<ScheduleDto>() {
        override fun areItemsTheSame(o: ScheduleDto, n: ScheduleDto) =
            o.scheduleId == n.scheduleId        // 고유 키 비교
        override fun areContentsTheSame(o: ScheduleDto, n: ScheduleDto) = o == n
    }
}

/* ──────────────── 확장 함수 : 날짜 포맷 예시 ──────────────── */
private fun OffsetDateTime?.toLocalDateTimeString(): String =
    this?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "—"
