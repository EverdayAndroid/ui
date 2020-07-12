package com.everday.skinplugin

import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.everday.skinplugin.utils.SkinThemeUtils

/**
 * @author wt
 * @date 1:01 2020/7/9
 * @description  存放
 **/
class SkinAttribute {
    //View属性集合
    private val mAttribute = ArrayList<String>()

    //记录换肤需要操作的View信息
    private val mSkinViews = ArrayList<SkinView>()
    //需要修改给的属性
    init {
        mAttribute.add("background")
        mAttribute.add("src")
        mAttribute.add("textColor")
        mAttribute.add("drawableLeft")
        mAttribute.add("drawableRight")
        mAttribute.add("drawableTop")
        mAttribute.add("drawableBottom")
    }

    /**
     * 对所有的View中的所有的属性进行皮肤修改
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun applySkin(){
        for (mSkinView in mSkinViews){
            mSkinView.applySkin()
        }
    }

    /**
     * 记录View身上哪几个需要换肤的属性
     * src、textColor....
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun hook(view:View, attrs:AttributeSet){
        val mSkinPars = ArrayList<SkinPair>()
        for (index in 0 .. attrs.attributeCount){
            //获取属性名 src、textColor....
            val attributeName = attrs.getAttributeName(index)
            if(mAttribute.contains(attributeName)){
                //获取到属性对应的value
                val attrValue = attrs.getAttributeValue(index)
                if (attrValue.startsWith("#")){
                    continue
                }
                var resId = 0

                //android:layout_height = "?actionBarSize"
                //以 ? 开头的表示使用 属性
                resId = if(attrValue.startsWith("?")){
                    val attrId = attrValue.substring(1).toInt()
                    SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                }else{
                    //正常以@ 开头
                    attrValue.substring(1).toInt()
                }
                val skinPair = SkinPair(attributeName, resId)
                mSkinPars.add(skinPair)
            }
        }

        if(!mSkinPars.isNullOrEmpty() || view is SkinViewSupport){
            val skinView = SkinView(view, mSkinPars)
            // 如果选择过皮肤 ，调用 一次 applySkin 加载皮肤的资源
            skinView.applySkin()
            mSkinViews.add(skinView)
        }
    }
}