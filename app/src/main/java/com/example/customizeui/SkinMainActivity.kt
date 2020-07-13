package com.example.customizeui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.customizeui.fragment.MusicFragment
import com.example.customizeui.fragment.RadioFragment
import com.example.customizeui.fragment.VideoFragment
import com.example.customizeui.widegt.MyTabLayout
import java.util.*

class SkinMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_main)
        val tabLayout = findViewById<MyTabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val list: MutableList<Fragment> = ArrayList<Fragment>()
        list.add(MusicFragment())
        list.add(VideoFragment())
        list.add(RadioFragment())
        val listTitle: MutableList<String> =
            ArrayList()
        listTitle.add("音乐")
        listTitle.add("视频")
        listTitle.add("电台")
        val myFragmentPagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, list, listTitle)
        viewPager.adapter = myFragmentPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * 进入换肤
     *
     * @param view
     */
    fun skinSelect(view: View?) {
        startActivity(Intent(this, SkinActivity::class.java))
    }
}