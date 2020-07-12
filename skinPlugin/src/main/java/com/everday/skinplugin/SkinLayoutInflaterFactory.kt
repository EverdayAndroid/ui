package com.everday.skinplugin

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.everday.skinplugin.utils.SkinThemeUtils
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.*

/**
 * @author wt
 * @date 15:33 2020/7/12
 * @description 用户来接管系统view的生产过程
 **/
class SkinLayoutInflaterFactory :LayoutInflater.Factory2,Observer{
    //记录view构造函数
    private val mConstructorSignature = arrayOf(
        Context::class.java, AttributeSet::class.java
    )
    //存放view的构造函数
    private val sConstructorMap =
        HashMap<String, Constructor<out View?>>()

    //当选择新皮肤后需要替换view与之的属性
    //页面属性管理器
    private var skinAttribute:SkinAttribute? = null
    //用于获取窗口的状态框的信息
    private var activity:Activity? = null

    constructor(activity: Activity){
        this.activity = activity
        skinAttribute = SkinAttribute()
    }

    private val mClassPrefixList = arrayListOf<String>(
        "android.widget",
        "android.webkit",
        "android.app",
        "android.view"
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        val view = createSDKView(name, context, attrs)
        if(view == null){
            createView(name, context, attrs)
        }
        if(view!=null){
            skinAttribute?.hook(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        TODO("Not yet implemented")
    }

    private fun createSDKView(name: String,context: Context,attrs: AttributeSet):View?{
        //如果包含，则不是SDK中的view 可能是自定义view包括support库中的view
        if(-1 != name.indexOf(".")){
            return null
        }
        //不包含就要在解析的 节点 name前  拼接：android.widget 尝试去反射
        for (index in 0 .. mClassPrefixList.size){
            val view = createView(mClassPrefixList[index] + name, context, attrs)
            if(view != null) {
                return view
            }
        }
        return null
    }

    private fun createView(name: String,context: Context,attrs: AttributeSet):View?{
        val findConstructor = findConstructor(context, name)
        try {
            return findConstructor?.newInstance(context, attrs)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    private fun findConstructor(context: Context,name:String):Constructor<out View>?{
        var constructor = sConstructorMap[name]
        if(constructor == null){
            try {
                val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                sConstructorMap[name] = constructor
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return constructor
    }

    //如果有人发送通知，这里就会执行
    @RequiresApi(Build.VERSION_CODES.M)
    override fun update(o: Observable?, arg: Any?) {
        SkinThemeUtils.updateStatusBarColor(activity)
        skinAttribute?.applySkin()
    }
}