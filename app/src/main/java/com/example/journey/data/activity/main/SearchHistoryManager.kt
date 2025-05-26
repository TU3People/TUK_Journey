package com.example.journey.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SearchHistoryManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)

    private val KEY = "search_history"

    fun getHistory(): MutableList<String> {
        val raw = prefs.getString(KEY, "") ?: ""
        Log.d("SearchHistory", "📦 불러온 기록: $raw")
        return if (raw.isEmpty()) mutableListOf() else raw.split("|").toMutableList()
    }


    fun addKeyword(keyword: String) {
        val history = getHistory()
        history.remove(keyword)
        history.add(0, keyword)
        val joined = history.joinToString("|")
        Log.d("SearchHistory", "🔒 저장됨: $joined") // ✅ 확인용 로그
        prefs.edit().putString(KEY, joined).apply()
    }


    fun clearHistory() {
        prefs.edit().remove(KEY).apply()
    }


}
