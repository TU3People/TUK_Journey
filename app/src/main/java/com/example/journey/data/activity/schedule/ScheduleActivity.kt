package com.example.journey.data.activity.schedule



import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.journey.data.activity.schedule.Slot
import com.example.journey.data.activity.schedule.toMinutes
import com.example.journey.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        tabFunc()

        /* 1) 서버 호출 → JSON → Slot 리스트 변환
              여기서는 하드코딩 예시 */
        val slots = listOf(
            Slot(1, "09:00".toMinutes(), "10:30".toMinutes(), "OS",   Color.parseColor("#FF7043")),
            Slot(2, "13:30".toMinutes(), "15:00".toMinutes(), "Math", Color.parseColor("#4CAF50"))
        )

        /* 2) Custom View(GridTimetable)에 리스트 투입 */
        binding.gridTimetable.submitSlots(slots)
    }

    private fun tabFunc(){

        val list1 = arrayListOf("일", "월", "화", "수", "목", "금", "토")

        for(i in 0..6){
            binding.schTab.addTab(binding.schTab.newTab().apply { text = list1[i] })

        }

        binding.schTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Toast.makeText(applicationContext, "d", Toast.LENGTH_LONG).show()
            }
        })

    }
}
