package com.example.customizeui

import android.app.Application
import com.everday.skinplugin.SkinManager

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}