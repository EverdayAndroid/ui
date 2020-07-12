package com.example.customizeui.widegt

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
/**
 * @author wt
 * @date 11:52 2020/7/5
 * @description  自定义流式布局
 *
 * 自定义View绘制流程
 * 1.onMeasure     测量view大小
 * 2.onLayout      确定子view布局
 * 3.onDraw        实际绘制内容
 *
 * 漏洞检测
 * 1.padding & margin  的处理
 * 2.lastLine遗漏
 * 3.visibleStae 遗漏
 * 4.
 **/
class FlowLayout:ViewGroup {
    private var flowWidth = 0
    private var flowHeight = 0

    //存放所有行集合
    private val lines = ArrayList<ArrayList<View>>()
    //每行存放的Views
    private val lineViews = ArrayList<View>()
    private val lineHeights = ArrayList<Int>()
    //行宽
    private var lineWidth = 0
    //一行已使用多少宽度
    private var lineUseWidth = 0
    //行高
    private var lineHeight = 0

    //FlowLayout 宽度
    private var selfWidth = 0
    //FlowLayout 高度
    private var selfHeight = 0
    //间距
    private var mHorizontalSpacing = 0
    //垂直间距
    private var mVerticalSpacing = 0

    constructor(context: Context):super(context)
    //反射
    constructor(context: Context,attr:AttributeSet):super(context,attr)
    //主题style
    constructor(context: Context,attr:AttributeSet,defStyleAttr:Int):super(context,attr,defStyleAttr)


    /**
     * onMeasure执行几次有父控件决定
     * 集合在onMeasure进行清空
     */
    private fun initMeasureParams(){
        lines.clear()
        lineViews.clear()
        lineHeights.clear()
        lineUseWidth = 0

    }

    //测量
    /**
     * MeasureSpec是View中的内部类，基本都是二进制运算，由Int是32位的，用高两位
     * 表示mode，低30表示size，MODE_SHIFT = 30 的作用是移位
     * MeasureSpec.EXACTLY          精确大小
     * MeasureSpec.AT_MOST          大小不可超过某数值
     * MeasureSpec.UNSPECIFIED      不对view大小做限制
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initMeasureParams()
        //获取FlowLayout控件宽度
        selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        //获取FlowLayout控件高度
        selfHeight = MeasureSpec.getSize(heightMeasureSpec)

        //获取当前View padding值
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        var parentNeededHeight  = 0
        var parentNeededWidth = 0
        for (index in 0 until childCount) {
            val childAt = getChildAt(index)
            val layoutParams = childAt.layoutParams
            //获取子View宽度 MeasureSpec
            val childWidthMeasureSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight,
                layoutParams.width
            )
            //获取子View高度 MeasureSpec
            val childHeightMeasureSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom,
                layoutParams.height
            )
            //测量子View
            childAt.measure(childWidthMeasureSpec,childHeightMeasureSpec)
            //获取子View测量之后宽度
            val childMeasuredWidth = childAt.measuredWidth
            //获取子View测量之后的高度
            val childMeasuredHeight = childAt.measuredHeight


            //测量子View  等同以上所有操作
//            measureChild(childAt,widthMeasureSpec,heightMeasureSpec)
            //处理换行操作
            if(childMeasuredWidth + lineUseWidth + mHorizontalSpacing > selfWidth){
                parentNeededHeight += lineHeight + mVerticalSpacing
                parentNeededWidth =
                    parentNeededWidth.coerceAtLeast(lineUseWidth + mHorizontalSpacing)
                lineHeights.add(lineHeight)
                lines.add(lineViews)
                lineViews.clear()
                lineUseWidth = 0
                lineHeight = 0
            }
            //记录当前行宽
            lineWidth = lineUseWidth.coerceAtLeast(lineWidth)
            //添加view
            lineViews.add(childAt)
            //每行都会有自己的宽和高
            lineUseWidth += childMeasuredWidth + mHorizontalSpacing
            lineHeight = lineHeight.coerceAtLeast(childMeasuredHeight)


            //处理最后一行
            if(index == childCount -1){
                lineHeights.add(lineHeight)
                lines.add(lineViews)
                parentNeededHeight += lineHeight + mVerticalSpacing
                parentNeededWidth = parentNeededWidth.coerceAtLeast(lineUseWidth + mHorizontalSpacing)
            }
        }

        //在测量自己，保存
        //根据子View的测量结果，来重新测量自己ViewGroup
        //作为一个ViewGroup，它自己也是一个View，它的大小也需要根据它的父亲给它提供的宽高来测量
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        flowWidth = if(widthMode == MeasureSpec.EXACTLY) selfWidth else parentNeededWidth
        flowHeight = if(heightMode == MeasureSpec.EXACTLY) selfHeight else parentNeededHeight
        //确定当前View宽高
        setMeasuredDimension(flowWidth,flowHeight)
    }

    //布局
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.e("TAG","onLayout")
        //处理padding
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        var left = paddingLeft
        var top = paddingTop
        var right = 0
        var bottom = 0
        for (index in 0 until lines.size) {  //    342    600
            val lineViews = lines[index]
            for (index in 0 until lineViews.size) {
                val childAt = lineViews[index]
                right += childAt.measuredWidth
                bottom = top + childAt.measuredHeight
                childAt.layout(left,top,right,bottom)
                left += right
            }
            Log.e("TAG","$top   $bottom")
            left = paddingLeft
            right = 0
            top += lineHeights[index]+paddingTop
        }

    }

    //绘制
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }



}