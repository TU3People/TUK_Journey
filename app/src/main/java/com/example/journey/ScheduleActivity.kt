package com.example.journey

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.intFloatMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.gson.annotations.SerializedName


class ScheduleActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        tabFunc()

    }

    private fun tabFunc(){

        val list1 = arrayListOf("일", "월", "화", "수", "목", "금", "토")

        for(i in 0..6){
            binding.schTab.addTab(binding.schTab.newTab().apply { text = list1[i] })

        }

        binding.schTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: Tab?) {

            }
            override fun onTabUnselected(tab: Tab?) {

            }

            override fun onTabSelected(tab: Tab?) {
                Toast.makeText(applicationContext, "d", Toast.LENGTH_LONG).show()
            }
        })

    }

}
///* 변환: 시간 → 분 */
//fun SlotDto.toCell(): Cell {
//    val startMin = start.toMinutes()     // "09:00" → 540
//    val endMin   = end.toMinutes()       // 630
//    return Cell(
//        row = startMin / 30,
//        col = day + 1,                   // 0열은 시간
//        rowSpan = (endMin - startMin) / 30,
//        slot = this
//    )
//}
private fun String.toMinutes(): Int =
    split(":").let { it[0].toInt()*60 + it[1].toInt() }

