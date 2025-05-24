package com.example.journey.data.remote

import android.app.Application
import android.content.Context

class Token : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
