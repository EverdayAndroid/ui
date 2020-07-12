package com.everday.skinplugin.desc

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class FieldName(val desc: String = "")