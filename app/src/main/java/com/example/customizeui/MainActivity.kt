package com.example.customizeui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.everday.skinplugin.SkinManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //换肤插件
        SkinManager.getInstance().loadSkin("")
    }
}