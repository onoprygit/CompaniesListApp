package com.onopry.lifehackstudiotesttask.app.presentation.screen.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import kotlin.math.roundToInt

class ScrollViewWithMap(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int,
    defStyleRes: Int
) : ScrollView(context, attrs, defStyle, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs, defStyle, 0)
    constructor(context: Context, attrs: AttributeSet?) : this (context, attrs, 0)
    constructor(context: Context) : this (context, null)


    private val interceptScrollView = mutableListOf<View>()

    fun addInterceptScrollView(view: View) {
        interceptScrollView.add(view)
    }

    fun removeInterceptScrollView(view: View) {
        interceptScrollView.remove(view)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (interceptScrollView.size > 0) {
            val x = ev.x.roundToInt()
            val y = ev.y.roundToInt()

            val bounds = Rect()

            interceptScrollView.forEach { v ->
                v.getHitRect(bounds)
                if (bounds.contains(x, y + scrollY))
                    return false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}