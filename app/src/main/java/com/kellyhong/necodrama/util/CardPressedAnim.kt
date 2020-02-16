package com.kellyhong.necodrama.util

import android.view.MotionEvent
import android.view.View

object CardPressedAnim {
	fun elevationPressed(elevationZ: Float, view: View) {
		view.setOnTouchListener(object : View.OnTouchListener {
			override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
				when {
					p1?.action == MotionEvent.ACTION_DOWN -> {
						p0?.elevation = 0f
						p0?.scaleY = 0.994f
						return true
					}
					p1?.action == MotionEvent.ACTION_UP -> {
						p0?.elevation = elevationZ
						p0?.scaleY = 1f
						return p0?.callOnClick() ?: false
					}
					p1?.action == MotionEvent.ACTION_CANCEL -> {
						p0?.elevation = elevationZ
						p0?.scaleY = 1f
					}
				}
				return false
			}
		})
	}

	fun splitPressed(view: View, vararg child: View) {
		view.setOnTouchListener { p0, p1 ->
			if(p1?.action == MotionEvent.ACTION_DOWN) {
				child.forEach {
					it.isPressed = true
				}
			}else if(p1?.action == MotionEvent.ACTION_UP || p1?.action == MotionEvent.ACTION_CANCEL) {
				child.forEach {
					it.isPressed = false
				}
			}
			false
		}
	}
}