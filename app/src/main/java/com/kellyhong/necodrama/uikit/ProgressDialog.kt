package com.kellyhong.necodrama.uikit

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.kellyhong.necodrama.R

class ProgressDialog {

	private var anim: ObjectAnimator? = null
	private var dialog: Dialog? = null

	fun showDialog(context: Context, title: Int) {
		if(dialog == null) {
			dialog = Dialog(context)
			dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
			dialog?.setCancelable(false)
			dialog?.setContentView(R.layout.dialog_loading_progress)
			dialog?.findViewById<View>(R.id.iv_progress)?.apply {
				val center = context.resources.getDimension(R.dimen.quadruple_space)
				this.pivotY = center
				this.pivotX = center
				anim = ObjectAnimator.ofFloat(this,"rotation", 0f, 360f)
				anim?.duration = 1200
				anim?.repeatCount = ObjectAnimator.INFINITE
				anim?.interpolator = LinearInterpolator()
			}
			dialog?.setOnShowListener {
				anim?.start()
			}
			dialog?.window?.setBackgroundDrawable(null)
		}
		dialog?.findViewById<TextView>(R.id.title)?.setText(title)
		dialog?.show()
		if(anim?.isPaused == true)
			anim?.resume()
	}

	fun hideDialog() {
		anim?.pause()
		dialog?.hide()
	}

	fun dismissDialog() {
		anim?.end()
		dialog?.dismiss()
	}
}