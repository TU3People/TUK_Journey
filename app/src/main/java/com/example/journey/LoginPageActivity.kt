package com.example.journey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.journey.databinding.ActivityLoginBinding
import com.example.journey.data.remote.model.auth.LoginRequest         // ✅ 새 경로
import com.example.journey.data.remote.network.RetrofitProvider        // ✅ 새 경로
import kotlinx.coroutines.launch

class LoginPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* … 웰컴 애니메이션 부분 동일 … */

        binding.buttonLogin.setOnClickListener {
            val idTxt = binding.loginTextId.text.toString()
            val pwTxt = binding.loginTextPassword.text.toString()

            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            lifecycleScope.launch {
                val req  = LoginRequest(idTxt, pwTxt)
                val resp = RetrofitProvider.authApi.login(req)   // ✅ 변경된 호출부
                val pref = Token.appContext
                    .getSharedPreferences("auth", Context.MODE_PRIVATE)

                if (resp.isSuccessful) {
                    resp.body()?.let { body ->
                        Toast.makeText(this@LoginPageActivity, body.message, Toast.LENGTH_SHORT).show()

                        if (body.result == "success") {
                            if (body.token.isNotEmpty()) {
                                pref.edit().putString("jwt_token", body.token).apply()
                            }
                            startActivity(mainIntent)
                        }
                    }
                } else {
                    Toast.makeText(this@LoginPageActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
                Log.d("token", pref.getString("jwt_token", null).toString())
            }
        }
    }
}
