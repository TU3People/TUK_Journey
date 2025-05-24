package com.example.journey.data.activity.schedule

import androidx.annotation.ColorInt

/** 30 분 단위 교시 1칸 */
data class Slot(
    val day: Int,               // 0=Sun,1=Mon…
    val startMin: Int,          // 0~1440
    val endMin: Int,
    val subject: String,
    @ColorInt val color: Int
) {
    val row     get() = startMin / 30
    val rowSpan get() = (endMin - startMin) / 30
}

/* 문자열 시간 → 분 변환 유틸 */
fun String.toMinutes(): Int =
    split(":").let { it[0].toInt() * 60 + it[1].toInt() }
