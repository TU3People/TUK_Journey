package com.example.Form

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.journey.databinding.ActivityLoginBinding

/* 1. 해당 파일을 복사 후, com.example.journey에 붙여넣으세요.
** 2. 복사 후, 아래의 class 다음에 defaultForm을 원하는 이름으로 변경하세요
** 3. ** 중요 ** layout에서 xml을 제작하고 xml이름을 activity_이름.xml으로 변경하세요.
** 4. 15번 줄의 binding = 다음을 Activity[xml 이름 맨 앞 대문자로 기제]Binding.xml 로 변경하세요.
** 5. 주석을 지우고 사용하세요
 */
class defaultForm : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // funtion 추가하세요
    }
}
