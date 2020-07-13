package com.example.customizeui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.everday.skinplugin.SkinManager

class SkinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin)
    }
    fun change(view: View?) {
        //换肤，收包裹，皮肤包是独立的apk包，可以来自网络下载
        SkinManager.getInstance().loadSkin("/data/data/com.enjoy.skin/skin/skin-debug.apk")
    }

    fun restore(view: View?) {
        SkinManager.getInstance().loadSkin(null)
    }
}