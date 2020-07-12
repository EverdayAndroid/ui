package com.everday.skinplugin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.everday.skinplugin.desc.FieldName
import com.everday.skinplugin.utils.SkinResources

/**
 * @author wt
 * @date 14:14 2020/7/12
 * @description 一个View对应多个属性
 **/
data class SkinView (
    val view:View,
    @FieldName(desc = "这个View能被换肤的属性于它对应的ID 集合")
    val skinPairs:ArrayList<SkinPair>
){
    @RequiresApi(Build.VERSION_CODES.M)
    fun applySkin(){
        applySkinAndroidX()
        for (skinPair in skinPairs){
            var left :Drawable? = null
            var top :Drawable? = null
            var right :Drawable? = null
            var bottom :Drawable? = null
            when(skinPair.attributeName){
                "background" ->{
                    val background = SkinResources.getInstance().getBackground(skinPair.resId)
                    //背景可能是color 也可能是drawable
                    if(background is Int){
                        view.setBackgroundColor(background)
                    }else if(background is Drawable){
                        ViewCompat.setBackground(view,background)
                    }
                }
                "src" ->{
                    val background = SkinResources.getInstance().getBackground(skinPair.resId)
                    if(view is ImageView){
                        if(background is Int){
                            view.setImageDrawable(ColorDrawable(background))
                        }else{
                            view.setImageDrawable(background as Drawable)
                        }
                    }
                }

                "textColor" ->{
                    if(view is TextView){
                        view.setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId))
                    }
                }

                "drawableLeft" ->{
                    left = SkinResources.getInstance().getDrawable(skinPair.resId)
                }
                "drawableTop" ->{
                    top = SkinResources.getInstance().getDrawable(skinPair.resId)
                }
                "drawableRight" ->{
                    right = SkinResources.getInstance().getDrawable(skinPair.resId)
                }
                "drawableBottom" ->{
                    bottom = SkinResources.getInstance().getDrawable(skinPair.resId)
                }
            }

            if(left !=null && top != null && right != null && bottom != null){
                if(view is TextView){
                    view.setCompoundDrawables(left,top,right, bottom)
                }
            }

        }
    }

    private fun applySkinAndroidX(){
        if(view is SkinViewSupport){
            view.applySkin()
        }
    }
}