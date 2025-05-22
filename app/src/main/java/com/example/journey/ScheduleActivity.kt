package com.example.journey

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.intFloatMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

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


    }
    fun tabFunc(){

        val list1 = arrayListOf("일", "월", "화", "수", "목", "금", "토")

        for(i in 0..6){

            tb.text = list1[i]
            binding.schTab.newTab(tb)
        }

    }
}

