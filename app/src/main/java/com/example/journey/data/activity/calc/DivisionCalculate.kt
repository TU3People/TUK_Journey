package com.example.journey.data.activity.calc

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.journey.databinding.ActivityDivisionCalculateBinding
import kotlin.math.roundToInt

class DivisionCalculate : AppCompatActivity() {
    lateinit var binding: ActivityDivisionCalculateBinding
    // 미리 저장된 비용 목록 (이름 → 금액)
    private val presetCosts = mapOf(
        "숙소비" to 50000.0,
        "식비"   to 20000.0,
        "교통비" to 10000.0
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDivisionCalculateBinding.inflate(layoutInflater)

        setContentView(binding.root)
// ➕ 버튼 클릭 리스너
        binding.addbtn.setOnClickListener {
            val options = arrayOf("미리 저장된 비용 추가", "직접 입력하기")
            AlertDialog.Builder(this)
                .setTitle("추가할 방식 선택")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> showPresetDialog()
                        1 -> showCustomInputDialog()
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }

        // 계산하기 버튼 클릭 리스너
        binding.btnCalc.setOnClickListener {
            val total  = binding.etTotal.text.toString().toDoubleOrNull() ?: 0.0
            val people = binding.etCount.text.toString().toIntOrNull() ?: 1

            if (people <= 0) {
                binding.tvResult.text = "인원 수를 1명 이상 입력하세요!"
                return@setOnClickListener
            }

            var share = total / people
            if (binding.swRound.isChecked) {
                share = (share / 100).roundToInt() * 100.0
            }
            binding.tvResult.text = "각자 ${share.toInt()}원씩 내면 됩니다!"
        }
    }

    // 미리 저장된 비용 선택 다이얼로그
    private fun showPresetDialog() {
        val items = presetCosts.keys.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("추가할 항목 선택")
            .setItems(items) { _, which ->
                val key = items[which]
                val addValue = presetCosts[key] ?: 0.0
                val current = binding.etTotal.text.toString().toDoubleOrNull() ?: 0.0
                val newTotal = current + addValue
                binding.etTotal.setText(newTotal.toInt().toString())
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 직접 금액 입력 다이얼로그
    private fun showCustomInputDialog() {
        val input = EditText(this).apply {
            hint = "추가할 금액을 입력하세요"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        AlertDialog.Builder(this)
            .setTitle("직접 금액 입력")
            .setView(input)
            .setPositiveButton("추가") { _, _ ->
                val value = input.text.toString().toDoubleOrNull() ?: 0.0
                val current = binding.etTotal.text.toString().toDoubleOrNull() ?: 0.0
                val newTotal = current + value
                binding.etTotal.setText(newTotal.roundToInt().toString())
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
