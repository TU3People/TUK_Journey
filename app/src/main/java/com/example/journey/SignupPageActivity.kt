package com.example.journey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.journey.databinding.ActivitySignuppageBinding
import com.example.utility.ResisterRequest
import com.example.utility.RetrofitClient
import kotlinx.coroutines.launch

class SignupPageActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivitySignuppageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idtext.setOnClickListener{
            finish()
        }

        binding.buttonSignup.setOnClickListener {
            var usr_em_txt = binding.registerTextEmail.text.toString()
            var usr_id_txt = binding.registerTextId.text.toString()
            var usr_pw_txt = binding.registerTextPassword.text.toString()
            var usr_pc_txt = binding.registerTextPasswordChk.text.toString()

            if (usr_pw_txt == usr_pc_txt){
                lifecycleScope.launch {
                    val request = ResisterRequest(usr_id_txt, usr_em_txt, usr_pw_txt)

                    val response = RetrofitClient.instance.register(request)
                    println(response)
                    if (response.isSuccessful) {
                        val result = response.body()
                        Toast.makeText(this@SignupPageActivity, result?.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignupPageActivity, "개발자 문의 접근 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@SignupPageActivity, "입력하신 비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}