package com.example.old

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.utility.ResisterRequest
import com.example.utility.RetrofitClient
import com.example.journey.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class Register : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
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
                        Toast.makeText(this@Register, result?.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Register, "개발자 문의 접근 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@Register, "입력하신 비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}