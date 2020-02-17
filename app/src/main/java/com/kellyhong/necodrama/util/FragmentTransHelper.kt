package com.kellyhong.necodrama.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kellyhong.necodrama.R

object FragmentTransHelper {

	fun<T: Fragment> showFragment(supportFragmentManager: FragmentManager,
	                              replaceLayoutId: Int,
	                              creator:()-> T): T {
		val transaction = supportFragmentManager.beginTransaction()
		val fragment = creator.invoke()
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
				   .replace(replaceLayoutId, fragment)
				.commitAllowingStateLoss()
		return fragment
	}

	fun<T: Fragment> addFragment(supportFragmentManager: FragmentManager,
	                              replaceLayoutId: Int,
	                              showTag: String,
	                              previous: Fragment?,
	                              creator:()-> T): Fragment {
		val transaction = supportFragmentManager.beginTransaction()
		val fragment = creator.invoke()
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
				                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
				   .add(replaceLayoutId, fragment, showTag)
		if(previous != null)
			transaction.hide(previous)
			transaction.addToBackStack(showTag)
				.commitAllowingStateLoss()
		return fragment
	}
}