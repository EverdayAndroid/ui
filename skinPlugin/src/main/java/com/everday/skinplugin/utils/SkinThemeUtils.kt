package com.everday.skinplugin.utils

import android.app.Activity
import android.content.Context
import android.os.Build


/**
 * @author wt
 * @date 0:22 2020/7/9
 * @description  状态栏
 **/
object SkinThemeUtils {
    //主题设置
    private val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(
        androidx.appcompat.R.attr.colorPrimaryDark)
    //状态栏，底部状态栏
    private val STATUS_BAR_COLOR_ATTRS = intArrayOf(
        android.R.attr.statusBarColor,
        android.R.attr.navigationBarColor)

    fun updateStatusBarColor(activity: Activity?){
        //5.0以上才能修改
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || activity == null){
            return
        }
        val resIds = getResId(activity, STATUS_BAR_COLOR_ATTRS)
        val statusBarColorResId = resIds[0]
        val navigationBarColorResId = resIds[1]
        //如果直接在style中写入固定颜色值(而不是@color/xxx)获取到的是0
        if(statusBarColorResId != 0){
            var color = SkinResources.getInstance().getColor(statusBarColorResId)
            activity.window.statusBarColor = color
        }else{
            //获取App默认状态栏颜色
            val color = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            activity.window.statusBarColor = color
        }
        //底部导航栏颜色
        if(navigationBarColorResId != 0){
            val color = SkinResources.getInstance().getColor(navigationBarColorResId)
            activity.window.navigationBarColor = color
        }

    }

    /**
     * @param context
     * @param attrs 属性
     * @return 获得theme中的属性中定义的  资源ID
     */
    fun getResId(context: Context,attrs:IntArray):ArrayList<Int>{
        val attributes = context.obtainStyledAttributes(attrs)
        val resIds = ArrayList<Int>()
        for (index in 0 .. attrs.size){
            resIds.add(attributes.getResourceId(index,0))
        }
        attributes.recycle()
        return resIds
    }

}