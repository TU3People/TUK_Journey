package com.example.journey.data.activity.rest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.R
import com.example.journey.data.activity.cafe.NcafeFragment
import com.example.journey.data.activity.cafe.ScafeFragment
import com.example.journey.databinding.ActivityRestBinding
import com.google.android.material.tabs.TabLayoutMediator

class RestActivity : AppCompatActivity() {
    lateinit var binding: ActivityRestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title=""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = RestPagerAdapter(this)
        binding.viewPager.adapter = adapter

        //tap 설정
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "주변 추천 맛집"
                1 -> "정왕역 근처 맛집"
                else -> "탭"
            }
        }.attach()

        binding.searchButton.setOnClickListener {
            val query = binding.cafesearch.text.toString().trim()
            val currentIndex = binding.viewPager.currentItem

            val fragment = (binding.viewPager.adapter as? RestPagerAdapter)?.getFragment(currentIndex)

            when (fragment) {
                is NrestFragment -> fragment.search(query)
                is SrestFragment -> fragment.search(query)
            }
        }


    }
}

class RestPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        NrestFragment(),
        SrestFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getFragment(position: Int): Fragment = fragmentList[position]
}