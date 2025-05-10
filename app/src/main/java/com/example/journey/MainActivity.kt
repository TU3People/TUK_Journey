package com.example.journey

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.databinding.ActivityMainBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var backPressedTime: Long = 0
    private lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggle = ActionBarDrawerToggle(this,binding.drawer, R.string.drawer_open, R.string.drawer_close)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        var isExpanded = false
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync{ naverMap ->
        }
        fun dpToPx(dp: Float): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }

        binding.handlewrapper.setOnClickListener {
            binding.root.post {
                val screenHeight = binding.root.height
                val moveDistance = screenHeight * 0.65f - dpToPx(150f)
                Log.d("SlideAnim", "screenHeight = $screenHeight")
                Log.d("SlideAnim", "moveDistance = $moveDistance")

                val translationY = if (isExpanded) 0f else moveDistance
                val targetAlpha = if (isExpanded) 1f else 0f

                binding.topbackimage.animate()
                    .alpha(targetAlpha)
                    .setDuration(200)
                    .withEndAction {
                        if(isExpanded) {
                            binding.topbackimage.visibility = View.VISIBLE
                        }
                        else{
                            binding.topbackimage.visibility = View.GONE
                        }
                    }
                    .start()


                binding.moveframe.animate()
                    .translationY(translationY)
                    .setDuration(300)
                    .withEndAction {
                        isExpanded = !isExpanded
                    }
                    .start()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) {
            finishAffinity()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}