package com.example.journey.data.activity.schedule

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.R
import com.example.journey.data.activity.activ.ActivPagerAdapter
import com.example.journey.data.activity.cafe.ActivFragment
import com.example.journey.data.activity.cafe.EntertainmentFragment
import com.example.journey.data.activity.cafe.NatureFragment
import com.example.journey.databinding.ActivityTimetableBinding
import com.google.android.material.tabs.TabLayoutMediator

class TimetableActivity : AppCompatActivity() {
    lateinit var binding : ActivityTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = ""
        val adapter = TimetablePagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.schTab, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "시간표"
                1 -> "시간표 리스트"
                2 -> "새 시간표"
                else -> "탭"
            }
        }.attach()

    }
}

