package com.example.journey.data.activity.Roulette

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class RouletteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    private var items: List<String> = listOf()
    private var anglePerItem = 0f
    private var rotationAngle = 0f

    fun setItems(newItems: List<String>) {
        items = newItems
        anglePerItem = if (items.isNotEmpty()) 360f / items.size else 0f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 60f
        var startAngle = rotationAngle

        for (i in items.indices) {
            // 섹터 색상 설정
            paint.color = Color.HSVToColor(floatArrayOf(i * 360f / items.size, 0.5f, 1f))

            // 섹터 그리기
            canvas.drawArc(
                centerX - radius, centerY - radius,
                centerX + radius, centerY + radius,
                startAngle, anglePerItem, true, paint
            )

            // 텍스트 위치 계산
            val angle = Math.toRadians((startAngle + anglePerItem / 2).toDouble())
            val textX = centerX + cos(angle) * radius / 1.5
            val textY = centerY + sin(angle) * radius / 1.5
            canvas.drawText(items[i], textX.toFloat(), textY.toFloat(), textPaint)

            startAngle += anglePerItem
        }
        // ... 원판과 텍스트 그린 후에

// 🔺 빨간 포인터
        val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.RED }
        val path = Path()
        val pointerWidth = 40f
        val pointerHeight = 40f
        path.moveTo(centerX, centerY - radius + 10f)
        path.lineTo(centerX - pointerWidth / 2, centerY - radius - pointerHeight + 10f)
        path.lineTo(centerX + pointerWidth / 2, centerY - radius - pointerHeight + 10f)
        path.close()

        canvas.drawPath(path, pointerPaint)

    }

    fun spinRandom(duration: Long = 3000L) {
        if (items.isEmpty()) return

        val randomIndex = (items.indices).random()
        val extraRotation = (0..360).random() // 회전감 랜덤 오프셋

        val targetAngle =
            360f * 5 + (items.size - randomIndex) * anglePerItem + extraRotation

        val animator = ValueAnimator.ofFloat(rotationAngle, rotationAngle + targetAngle)
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            rotationAngle = it.animatedValue as Float % 360
            invalidate()
        }
        animator.start()
    }

    fun spinAndReturnResult(duration: Long = 3000L, onResult: (Int) -> Unit) {
        if (items.isEmpty()) return

        val randomIndex = (items.indices).random()
        val extraRotation = (0..360).random().toFloat()

        val totalRotation = 360f * 5 + (items.size - randomIndex) * anglePerItem + extraRotation

        val animator = ValueAnimator.ofFloat(rotationAngle, rotationAngle + totalRotation)
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            rotationAngle = it.animatedValue as Float % 360
            invalidate()
        }

        animator.doOnEnd {
            // 화면 상단 포인터 기준으로 계산
            val pointerAngle = (270f - rotationAngle + 360f) % 360f
            val landedIndex = (pointerAngle / anglePerItem).toInt() % items.size

            onResult(landedIndex)
        }

        animator.start()
    }




}
