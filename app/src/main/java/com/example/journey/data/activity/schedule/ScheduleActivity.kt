package com.example.journey.data.activity.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.R
import com.example.journey.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout

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