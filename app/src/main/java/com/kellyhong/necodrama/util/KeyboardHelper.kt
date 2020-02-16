package com.kellyhong.necodrama.util

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    /**
     * show the software keyboard
     * @param activity
     */
    fun show(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * hide the software keyboard
     * @param activity
     */
    fun hide(activity: Activity?) {
        if (activity != null) {
            val view = activity.currentFocus
            if (view != null) {
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }
    }
}