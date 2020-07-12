package com.everday.skinplugin

import com.everday.skinplugin.desc.FieldName
/**
 * @author wt
 * @date 14:14 2020/7/12
 * @description view属性
 **/
data class SkinPair(
    @FieldName(desc = "属性名")
    val attributeName:String,
    @FieldName(desc = "对应的资源ID")
    val resId:Int
)