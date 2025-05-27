package com.example.journey.data.activity.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.data.activity.cafe.ActivFragment
import com.example.journey.data.activity.cafe.EntertainmentFragment
import com.example.journey.data.activity.cafe.NatureFragment
import com.example.journey.ui.timetable.TimetableFragment

class TimetablePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        TimetableFragment(),
        TimetableListFragment(),
        TimetableNewFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getFragment(position: Int): Fragment = fragmentList[position]
}