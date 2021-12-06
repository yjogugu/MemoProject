package com.taijoo.potfolioproject.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView


class CustomRecyclerViewLayout(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private val mTouchSlop: Int
    private var mPrevX = 0f
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = MotionEvent.obtain(e).x
            MotionEvent.ACTION_MOVE -> {
                val eventX = e.x
                val xDiff = Math.abs(eventX - mPrevX)
                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }
}
