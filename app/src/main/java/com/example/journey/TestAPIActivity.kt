package com.example.journey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.journey.databinding.ActivitySignuppageBinding
import com.example.journey.databinding.ActivityTestapiBinding
import com.example.utility.ResisterRequest
import com.example.utility.RetrofitClient
import kotlinx.coroutines.launch

class TestAPIActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityTestapiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testButton.setOnClickListener{
            var reqData = binding.testReqdata.text.toString()
            lifecycleScope.launch {
                val request = ResisterRequest("","","")
                val response = RetrofitClient.instance.register(request)

                if (response.isSuccessful) {
                    val result = response.body()
                    Toast.makeText(this@TestAPIActivity, result?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@TestAPIActivity, "개발자 문의 접근 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}