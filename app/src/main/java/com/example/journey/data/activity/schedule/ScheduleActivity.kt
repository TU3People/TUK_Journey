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

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    val titles = arrayListOf("시간표", "시간표 리스트", "새 시간표")

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
