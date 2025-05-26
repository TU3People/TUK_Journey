package com.example.journey.data.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.R
import com.example.journey.databinding.ActivityFixprofileBinding

class FixprofileActivity : AppCompatActivity() {
    lateinit var binding: ActivityFixprofileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFixprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title=""
        val nickname = intent.getStringExtra("nickname")
        binding.profilename.setText(nickname)
        binding.profilecheckbt.setOnClickListener {
            var name = binding.profilename.text.toString()

            val pref = getSharedPreferences("profile", Context.MODE_PRIVATE)
            pref.edit().putString("nickname", name).apply()

            val intent = Intent()
            intent.putExtra("name",name)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}