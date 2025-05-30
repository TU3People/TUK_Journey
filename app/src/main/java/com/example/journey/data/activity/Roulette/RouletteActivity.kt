package com.example.journey.data.activity.Roulette

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.R
import com.example.journey.databinding.ActivityRouletteBinding
import kotlin.random.Random

class RouletteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouletteBinding
    private val itemList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRouletteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""

        // 항목 추가
        binding.addItemBtn.setOnClickListener {
            val input = binding.inputItem.text.toString().trim()
            if (input.isNotEmpty()) {
                addItem(input)
                binding.inputItem.text.clear()
            }
        }

        // 룰렛 생성
        binding.createRouletteBtn.setOnClickListener {
            val personCount = binding.inputPerson.text.toString().toIntOrNull()
            if (personCount == null || personCount < 1) {
                Toast.makeText(this, "인원 수를 정확히 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalList = distributeItems(itemList, personCount)
            binding.rouletteView.setItems(finalList)
        }

        // 룰렛 돌리기
        binding.spinBtn.setOnClickListener {
            if (itemList.isEmpty()) {
                Toast.makeText(this, "항목을 먼저 추가하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.rouletteView.spinAndReturnResult { index ->
                val selected = itemList[index % itemList.size]
                Toast.makeText(this, "당첨: $selected", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "돌리는 중...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun distributeItems(original: List<String>, count: Int): List<String> {
        if (original.isEmpty()) return listOf("없음")
        return List(count) { i -> original[i % original.size] }
    }

    private fun addItem(item: String) {
        val textView = TextView(this).apply {
            text = item
            textSize = 16f
            setPadding(16, 16, 16, 16)
            setBackgroundResource(android.R.color.darker_gray)
            setTextColor(Color.WHITE)

            setOnClickListener {
                binding.itemContainer.removeView(this)
                itemList.remove(item)
                Toast.makeText(context, "삭제됨: $item", Toast.LENGTH_SHORT).show()
            }
        }

        binding.itemContainer.addView(textView)
        itemList.add(item)
    }
}
