package com.example.journey.data.activity.activ

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.R
import com.example.journey.data.activity.cafe.ActivFragment
import com.example.journey.data.activity.cafe.EntertainmentFragment
import com.example.journey.data.activity.cafe.NatureFragment
import com.example.journey.databinding.ActivityActivBinding
import com.google.android.material.tabs.TabLayoutMediator

class ActivActivity : AppCompatActivity() {
    lateinit var binding: ActivityActivBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActivBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = ActivPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "자연"
                1 -> "오락"
                2 -> "액티비티"
                else -> "탭"
            }
        }.attach()

        binding.searchButton.setOnClickListener {
            val query = binding.cafesearch.text.toString().trim()
            val currentIndex = binding.viewPager.currentItem

            val fragment = (binding.viewPager.adapter as? ActivPagerAdapter)?.getFragment(currentIndex)

            when (fragment) {
                is NatureFragment -> fragment.search(query)
                is EntertainmentFragment -> fragment.search(query)
                is ActivFragment -> fragment.search(query)
            }
        }
    }
}

class ActivPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        NatureFragment(),
        EntertainmentFragment(),
        ActivFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getFragment(position: Int): Fragment = fragmentList[position]
}
