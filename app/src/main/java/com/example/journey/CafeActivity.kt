package com.example.journey

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.journey.databinding.ActivityCafeBinding
import com.google.android.material.tabs.TabLayoutMediator

class CafeActivity : AppCompatActivity() {
    lateinit var binding : ActivityCafeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCafeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title=""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = CafePagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "주변 추천 카페"
                1 -> "별점 높은 카페"
                else -> "탭"
            }
        }.attach()

        binding.searchButton.setOnClickListener {
            val query = binding.cafesearch.text.toString().trim()
            val currentIndex = binding.viewPager.currentItem

            val fragment = (binding.viewPager.adapter as? CafePagerAdapter)?.getFragment(currentIndex)

            if (fragment is NcafeFragment) {
                fragment.search(query)
            }

        }

    }
}

class CafePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        NcafeFragment(),
        ScafeFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getFragment(position: Int): Fragment = fragmentList[position]
}
