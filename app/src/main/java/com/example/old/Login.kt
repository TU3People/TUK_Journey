package com.example.old

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.utility.LoginRequest
import com.example.utility.RetrofitClient
import com.example.journey.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class Login : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            var usr_id_txt = binding.loginTextId.text.toString()
            var usr_pw_txt = binding.loginTextPassword.text.toString()

            lifecycleScope.launch {
                val request = LoginRequest(usr_id_txt, usr_pw_txt)

                val response = RetrofitClient.instance.login(request)
                // println(response)
                if (response.isSuccessful) {
                    val result = response.body()
                    Toast.makeText(this@Login, result?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Login, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
