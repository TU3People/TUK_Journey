package com.example.journey

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        binding.profilecheckbt.setOnClickListener {
            var name = binding.profilename.text.toString()
            val intent = Intent()
            intent.putExtra("name",name)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}