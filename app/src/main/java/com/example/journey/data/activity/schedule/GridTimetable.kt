package com.example.journey.data.activity.schedule

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.setMargins
import com.example.journey.R
import com.example.journey.data.activity.schedule.Slot

/** “48×8 그리드” Custom View */
class GridTimetable @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : GridLayout(ctx, attrs) {

    private val rowH = (48 * resources.displayMetrics.density).toInt() / 4   // 48dp

    init {
        columnCount = 8
        rowCount    = 48
        setPadding(4, 4, 4, 4)
    }

    /** 슬롯 리스트 + 빈칸을 이용해 그리드를 구성 */
    fun submitSlots(slots: List<Slot>) {
        removeAllViews()

        /* 48×8 null 초기화 */
        val map = Array(48) { Array<Slot?>(8) { null } }
        slots.forEach { s ->
            map[s.row][s.day + 1] = s            // head
            for (i in 1 until s.rowSpan)
                map[s.row + i][s.day + 1] = Slot(-1,0,0,"",Color.TRANSPARENT) // tail
        }

        for (r in 0 until 48) {
            for (c in 0 until 8) {
                val slot = map[r][c]
                if (slot != null && slot.day == -1) continue        // tail skip

                addView(buildCell(r, c, slot))
            }
        }
    }

    /** 개별 셀(TextView) 생성 */
    private fun buildCell(r: Int, c: Int, slot: Slot?): TextView {
        val tv = TextView(context).apply {
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.cell_border)

            when {
                c == 0 -> text = String.format("%02d:%02d", r / 2, if (r % 2 == 0) 0 else 30)
                slot != null -> {
                    text = slot.subject
                    setBackgroundColor(slot.color)
                    setTextColor(Color.WHITE)
                }
            }
        }

        val rowSpan = slot?.rowSpan ?: 1
        val lp = LayoutParams(
            spec(r, rowSpan, 1f),   // rowSpec
            spec(c, 1,      1f)    // colSpec
        ).apply {
            width  = 0;  height = rowH
            setMargins(1)
        }
        tv.layoutParams = lp
        return tv
    }
}