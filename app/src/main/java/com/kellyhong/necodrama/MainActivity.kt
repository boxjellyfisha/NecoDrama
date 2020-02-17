package com.kellyhong.necodrama

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.ui.main.DramaDetailFragment
import com.kellyhong.necodrama.ui.main.DramaListFragment
import com.kellyhong.necodrama.ui.main.MainViewModel
import com.kellyhong.necodrama.uikit.ProgressDialog
import com.kellyhong.necodrama.util.FragmentTransHelper
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.java.KoinJavaComponent.getKoin

class MainActivity : AppCompatActivity() {

    // create scope instance
    private val myScope = getKoin().getOrCreateScope<MainActivity>("MainActivity")
    // retrieve ViewModel instance from scope
    private val viewModel: MainViewModel by myScope.viewModel(this)
    private val loadingDialog by lazy { ProgressDialog() }
    private var listFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            listFragment = FragmentTransHelper.showFragment(supportFragmentManager, R.id.container) {
                DramaListFragment.newInstance()
            }
        }
        bindData()
    }

    override fun onDestroy() {
        super.onDestroy()
        myScope.close()
        loadingDialog.dismissDialog()
        listFragment = null
    }

    private fun bindData() {
        viewModel.loadingEvent.observe(this, Observer {
            if(it == -1)
                loadingDialog.hideDialog()
            else
                loadingDialog.showDialog(this, it)
        })
        viewModel.toastResId.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    fun gotoDetail(drama: Drama) {
        FragmentTransHelper.addFragment(supportFragmentManager, R.id.container,
            "detail", listFragment) {
            DramaDetailFragment.newInstance(drama)
        }
    }

    fun showSnackBarWithCheck(rootView: View, tipResId: Int, onClick: () -> Unit) {
        if(tipResId != 0 && !isFinishing) {
            val snack = buildSnack(rootView, tipResId)
            snack.duration = BaseTransientBottomBar.LENGTH_LONG
            snack.setAction(R.string.dialog_retry) {
                snack.dismiss()
                onClick.invoke()
            }
            snack.show()
        }
    }

    private fun buildSnack(rootView: View, tipResId: Int): Snackbar {
        val snack = Snackbar.make(rootView, tipResId, Snackbar.LENGTH_SHORT)
        val textView = snack.view.findViewById(
            com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(baseContext, R.color.text_black))
        snack.view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorPrimary))
        return snack
    }
}
