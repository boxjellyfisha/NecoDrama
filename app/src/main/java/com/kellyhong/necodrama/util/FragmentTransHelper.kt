package com.kellyhong.necodrama.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
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

	fun<T: Fragment> swapFragmentLife(supportFragmentManager: FragmentManager,
	                                  replaceLayoutId: Int,
	                                  showTag: String,
	                                  creator:()-> T) {
		val transaction = supportFragmentManager.beginTransaction()
		var fragment = supportFragmentManager.findFragmentByTag(showTag)
		if (fragment == null) {
			fragment = creator.invoke()
			addFragmentLife(supportFragmentManager, transaction,
					replaceLayoutId, fragment, showTag)
		} else {
			showFragmentLife(supportFragmentManager, transaction, fragment)
		}
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		transaction.commitAllowingStateLoss()
	}

	fun addFragmentLife(supportFragmentManager: FragmentManager,
	                    transaction: FragmentTransaction,
	                    replaceLayoutId: Int,
	                    f: Fragment, pageName: String): FragmentTransaction {
		if(supportFragmentManager.primaryNavigationFragment != null) {
			transaction
					.setMaxLifecycle(supportFragmentManager.primaryNavigationFragment!!, Lifecycle.State.STARTED)
					.add(replaceLayoutId, f, pageName)
					.hide(supportFragmentManager.primaryNavigationFragment!!)
					.setPrimaryNavigationFragment(f)
		} else
			transaction
					.add(replaceLayoutId, f, pageName)
					.setPrimaryNavigationFragment(f)
		transaction.setMaxLifecycle(f, Lifecycle.State.RESUMED)
		return transaction
	}

	fun showFragmentLife(supportFragmentManager: FragmentManager,
	                             transaction: FragmentTransaction, f: Fragment): FragmentTransaction {
		transaction
				.setMaxLifecycle(supportFragmentManager.primaryNavigationFragment!!, Lifecycle.State.STARTED)
				.show(f)
				.hide(supportFragmentManager.primaryNavigationFragment!!)
				.setPrimaryNavigationFragment(f)
				.setMaxLifecycle(f, Lifecycle.State.RESUMED)
		return transaction
	}
}