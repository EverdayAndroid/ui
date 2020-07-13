package com.everday.skinplugin

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import com.everday.skinplugin.utils.SkinResources
import java.util.*

class SkinManager() : Observable() {
    private lateinit var mContext : Application
    companion object{
        private var INSTANCE:SkinManager? = null
        fun init(application: Application):SkinManager = INSTANCE?: synchronized(this){
            INSTANCE?:SkinManager(application).also { INSTANCE = it }
        }

        fun getInstance():SkinManager = INSTANCE!!
    }

    constructor(application: Application) : this() {
        this.mContext = application
        //共享选项 用于记录当前使用过的皮肤
        SkinPreference.init(mContext)
        //资源管理类  用于从 皮肤apk加载资源
        SkinResources.init(mContext)
        val applicationActivityLifeCycle = ApplicationActivityLifeCycle(this)
        application.registerActivityLifecycleCallbacks(applicationActivityLifeCycle)

        loadSkin(SkinPreference.getInstance().skin)
    }

    /**
     * 记载皮肤并应用
     * @param skinPath 皮肤路径 如果为空则使用默认皮肤
     */
    fun loadSkin(skinPath:String?){
        if(skinPath.isNullOrEmpty()){
            SkinPreference.getInstance().reset()
            SkinResources.getInstance().reset()
        }else{
            val appResources = mContext.resources
            //反射创建AssetManager
            val assetManager = AssetManager::class.java.newInstance()

            val sSystemApkAssets = assetManager.javaClass.getDeclaredField("sSystemApkAssets")
            sSystemApkAssets.isAccessible = true

            val method = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            method.invoke(assetManager,skinPath)

            val skinResources =
                Resources(assetManager, appResources.displayMetrics, appResources.configuration)
            val packageManager = mContext.packageManager
            val packageArchiveInfo =
                packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            val packageName = packageArchiveInfo.packageName
            SkinResources.getInstance().applySkin(skinResources,packageName)
            SkinPreference.getInstance().skin = skinPath
        }
        //通知采集的View 更新皮肤
        //被观察者改变 通知所有观察者
        setChanged()
        notifyObservers("")
    }


}