package com.everday.skinplugin.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi

class SkinResources {
    private var mAppResources:Resources
    constructor(context: Context){
        mAppResources = context.resources
    }

    companion object{
        var mSkinPkgName:String? = null
        var isDefaultSkin = true
        //app原始resource

        //皮肤包的resource
        private var mSkinResources:Resources? = null
        private var INSTANCE:SkinResources? = null
        fun init(context: Context):SkinResources = INSTANCE?: synchronized(this){
            INSTANCE?:SkinResources(context).also {
                INSTANCE = it
            }
        }
        fun getInstance():SkinResources = INSTANCE!!
    }

    /**
     * 重置
     */
    fun reset(){
        mSkinPkgName = null
        mSkinResources = null
        isDefaultSkin = true
    }

    fun applySkin(resources: Resources,packName:String){
        mSkinResources = resources
        mSkinPkgName = packName
        isDefaultSkin = packName.isNullOrEmpty()
    }

    /**
     * 1.通过原始app中的resId获取到自己的名字
     * 2.根据名字和类型获取皮肤包中的资源ID
     */
    private fun getIdentifier(resId:Int):Int{
        if(isDefaultSkin){
            return resId
        }
        val resName = mAppResources?.getResourceName(resId)
        val resType = mAppResources?.getResourceTypeName(resId)
        return mSkinResources?.getIdentifier(resName,resType,mSkinPkgName)?:0
    }
    /**
     * 可能是color，也可能是drawable
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun getBackground(resId:Int):Any{
        val resourceTypeName = mAppResources.getResourceTypeName(resId)
        return if(resourceTypeName == "color"){
            getInstance().getColor(resId)
        }else{
            getInstance().getDrawable(resId)
        }
    }

    /**
     * @param resId 资源ID
     * @return 皮肤资源ID
     * 获取皮肤包资源ID
     * 如果获取不到则使用宿主APP资源ID
     */
    fun getColor(resId:Int):Int{
        if(isDefaultSkin){
            return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                mAppResources.getColor(resId,null)
            }else{
                mAppResources.getColor(resId)
            }

        }
        val skinId = getIdentifier(resId)
        if(skinId == 0 || mSkinResources == null){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                mAppResources.getColor(resId,null)
            }else{
                mAppResources.getColor(resId)
            }
        }
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            mSkinResources!!.getColor(resId,null)
        }else{
            mSkinResources!!.getColor(resId)
        }
    }

    fun getColorStateList(resId: Int):ColorStateList{
        if(isDefaultSkin){
            return mAppResources.getColorStateList(resId)
        }
        val skinId = getIdentifier(resId)
        if(skinId == 0){
            return mAppResources.getColorStateList(resId)
        }
        return mSkinResources!!.getColorStateList(resId)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getDrawable(resId:Int): Drawable {
        if(isDefaultSkin){
            return mAppResources.getDrawable (resId,null)
        }
        val skinId = getIdentifier(resId)
        if(skinId == 0 || mSkinResources == null){
            return mAppResources.getDrawable (resId,null)
        }
        return mSkinResources!!.getDrawable(resId,null)
    }
}