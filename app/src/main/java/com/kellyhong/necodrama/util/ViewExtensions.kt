package com.kellyhong.necodrama.util

import android.view.View

/** Milliseconds used for UI animations */
const val ANIMATION_SLOW_MILLIS = 400L

/**
 * Simulate a button click, including a small delay while it is being pressed to trigger the
 * appropriate animations.
 */
fun View.setOnSlowClickListener(
    clicker: View.OnClickListener,
    delay: Long = ANIMATION_SLOW_MILLIS
) {
    this.setOnClickListener {
        it.isEnabled = false
        it.postDelayed({ it.isEnabled = true }, delay)
        clicker.onClick(it)
    }
}