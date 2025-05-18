package com.example.journey


import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.databinding.ActivityRouletteBinding
import kotlin.random.Random



class RouletteActivity : AppCompatActivity() {
    lateinit var binding: ActivityRouletteBinding
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
        title=""

        binding.addItemBtn.setOnClickListener {
            val item = binding.inputItem.text.toString().trim()
            if (item.isNotEmpty()) {
                itemList.add(item)
                Toast.makeText(this, "추가됨: $item", Toast.LENGTH_SHORT).show()
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

        // 돌리기
        binding.spinBtn.setOnClickListener {
            if (itemList.isEmpty()) {
                Toast.makeText(this, "항목을 먼저 추가하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentItems = itemList
            val randomIndex = Random.nextInt(currentItems.size)
            binding.rouletteView.spinTo(randomIndex)

            // 결과는 회전 애니메이션 끝난 후 콜백으로 처리할 수도 있음
            Toast.makeText(this, "돌리는 중...", Toast.LENGTH_SHORT).show()
        }

        binding.addItemBtn.setOnClickListener {
            val input = binding.inputItem.text.toString().trim()
            if (input.isNotEmpty()) {
                addItem(input)
                binding.inputItem.text.clear()
            }
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

            // 클릭 시 삭제
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