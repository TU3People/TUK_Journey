package com.example.journey.data.activity.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.journey.data.activity.main.MainActivity
import com.example.journey.data.remote.Token
import com.example.journey.data.remote.model.auth.LoginRequest
import com.example.journey.data.remote.network.RetrofitProvider
import com.example.journey.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcombt.setOnClickListener{
            binding.mainImageView.animate()
                .translationY(-400f)
                .setDuration(300)
                .start()

            binding.welcome.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction{binding.welcome.visibility = View.GONE}
                .start()

            binding.welcombt.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction{binding.welcombt.visibility = View.GONE}
                .start()

            binding.loginForm.visibility = View.VISIBLE
            binding.loginForm.animate()
                .alpha(1f)
                .translationY(-300f)
                .setDuration(300)
                .start()
        }
        binding.idtext.setOnClickListener{
            val intent = Intent(this, SignupPageActivity::class.java)
            startActivity(intent)
        }

        /* Login Button */
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
                val prefProfile = Token.appContext
                    .getSharedPreferences("profile", Context.MODE_PRIVATE)

                if (resp.isSuccessful) {
                    resp.body()?.let { body ->
                        Toast.makeText(this@LoginPageActivity, body.message, Toast.LENGTH_SHORT).show()

                        if (body.result == "success") {
                            if (body.token.isNotEmpty()) {
                                pref.       edit().putString("jwt_token", body.token).apply()
                                prefProfile.edit().putString("id",        body.id).apply()
                                prefProfile.edit().putString("username",  body.username).apply()
                                prefProfile.edit().putString("useremail", body.useremail).apply()
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
