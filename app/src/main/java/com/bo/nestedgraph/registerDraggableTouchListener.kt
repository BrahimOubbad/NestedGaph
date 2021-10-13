package com.bo.nestedgraph

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

fun View.registerDraggableTouchListener(
    initialPosition: () -> PointF,
    positionListener: (x: Float, y: Float) -> Unit,
    onUp: () -> Unit,
    showCloseButton: (isShow: Boolean) -> Unit
) {
    WindowHeaderTouchListener(
        context,
        this,
        initialPosition,
        positionListener,
        onUp,
        showCloseButton
    )
}

class WindowHeaderTouchListener(
    val context: Context,
    view: View,
    private val initialPosition: () -> PointF,
    private val positionListener: (x: Float, y: Float) -> Unit,
    private val onUp: () -> Unit,
    private val showCloseButton: (isShow: Boolean) -> Unit,
) : View.OnTouchListener, GestureDetector.OnGestureListener {

    private var mDetector: GestureDetectorCompat
    private var isShow = false


    private var pointerStartX = 0
    private var pointerStartY = 0
    private var initialX = 0f
    private var initialY = 0f

    init {
        view.setOnTouchListener(this)
        mDetector = GestureDetectorCompat(context, this)

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        when (motionEvent.action) {

            MotionEvent.ACTION_DOWN -> {
                pointerStartX = motionEvent.rawX.toInt()
                pointerStartY = motionEvent.rawY.toInt()
                with(initialPosition()) {
                    initialX = x
                    initialY = y
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = motionEvent.rawX - pointerStartX
                val deltaY = motionEvent.rawY - pointerStartY
                positionListener(initialX + deltaX.toInt(), initialY + deltaY.toInt())
            }

            MotionEvent.ACTION_UP -> {
                onUp()
                view.performClick()
            }
        }
        return mDetector.onTouchEvent(motionEvent)
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d("DEBUG_TAG", "onDown")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d("DEBUG_TAG", "onFling")
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        onLongClick()
        Log.d("DEBUG_TAG", "onLongPress")

    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d("DEBUG_TAG", "onScroll")
        isShow = false
        showCloseButton(isShow)
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d("DEBUG_TAG", "onShowPress")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.d("DEBUG_TAG", "onSingleTapUp")

        if (isShow) {
            onLongClick()
        }

        return true
    }

    private fun onLongClick() {
        isShow = !isShow
        showCloseButton(isShow)
    }
}

