package com.everday.skinplugin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.everday.skinplugin.utils.SkinThemeUtils
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ApplicationActivityLifeCycle :Application.ActivityLifecycleCallbacks {
    //被观察者
    private var mObservable:Observable? = null
    private val mLayoutInflaterFactories = HashMap<Activity,SkinLayoutInflaterFactory>()

    constructor(observable: Observable){
        this.mObservable = observable
    }

    override fun onActivityPaused(activity: Activity) {



    }

    override fun onActivityStarted(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        val observer = mLayoutInflaterFactories.remove(activity)
        SkinManager.getInstance().deleteObserver(observer)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //更新状态栏
        SkinThemeUtils.updateStatusBarColor(activity)

        val layoutInflater = activity.layoutInflater
        try {
            //Android 布局加载器使用mFactorySet 标记是否设置过工厂
            //如果设置过则进行修改变量  mFactorySet属性值为false
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.set(layoutInflater,false)
        }catch (e:Exception){
            e.printStackTrace()
        }
        //设置自定义工厂模式
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory(activity)
        //设置工厂
        LayoutInflaterCompat.setFactory2(layoutInflater,skinLayoutInflaterFactory)
        //记录每个activity对应的工厂
        mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory
        //添加监听
        mObservable?.addObserver(skinLayoutInflaterFactory)
    }

    override fun onActivityResumed(activity: Activity) {
        TODO("Not yet implemented")
    }
}