package com.kellyhong.necodrama.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.kellyhong.necodrama.R

object ImageLoader {
	/**
	 * use transition will let the origin picture changed and the radio being weird.
	 */
	private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

	fun updatePhoto(view: ImageView, url: String, @DrawableRes placeholder: Int) {
		GlideApp.with(view.context)
				.load(url)
				.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
				.placeholder(placeholder)
				.centerCrop()
				.into(view)
	}

	fun updatePhoto(view: ImageView, url: String?) {
		if(!url.isNullOrEmpty())
			GlideApp.with(view.context)
					.load(url)
					.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
					.placeholder(R.drawable.image_empty)
					.centerCrop()
					.into(view)
	}
}