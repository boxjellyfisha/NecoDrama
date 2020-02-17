package com.kellyhong.necodrama.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kellyhong.necodrama.MainActivity
import com.kellyhong.necodrama.R
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.ui.adapter.ResultListAdapter
import com.kellyhong.necodrama.uikit.ItemDecoration
import com.kellyhong.necodrama.util.KeyboardHelper
import com.kellyhong.necodrama.util.setOnSlowClickListener
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.scope.getViewModel
import org.koin.java.KoinJavaComponent
import android.widget.ArrayAdapter




class DramaListFragment: Fragment() {

    companion object {
        fun newInstance() = DramaListFragment()
    }

    private var isInitData = false
    private val myScope = KoinJavaComponent.getKoin().getScope("MainActivity")
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = myScope.getViewModel(activity!!)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindData()
        getDramas()
    }

    private var resultAdapter: ResultListAdapter? = null
    private val onResultClicker by lazy {
        object : ResultListAdapter.OnItemClicker {
            override fun onClick(result: Drama?) {
                if(result != null)
                    gotoDetail(result)
            }
        }
    }
    private fun initView() {
        val padding = resources.getDimensionPixelSize(R.dimen.triple_space)
        rv_result.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(ItemDecoration(padding, padding, padding))
        }

        et_search.threshold = 1
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                et_search.isActivated = text?.isNotBlank() == true
            }
        })
        et_search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                searchByKeyword()
                return@OnEditorActionListener true
            }
            false
        })
        iv_search_clear.setOnSlowClickListener(View.OnClickListener {
            iv_search_clear.isVisible = false
            et_search.setText("")
            clearEtFocus(et_search)
            viewModel.getLocalDrams()
        })
    }

    private fun searchByKeyword() {
        if (et_search.isActivated) {
            clearEtFocus(et_search)
            viewModel.searchDrama(et_search.text.toString().trim())
            iv_search_clear.isVisible = true
        }
    }

    private fun bindData() {
        viewModel.dramas.observe(this as LifecycleOwner, Observer {
            if(resultAdapter == null) {
                resultAdapter = ResultListAdapter(it, onResultClicker)
                rv_result.adapter = resultAdapter
            } else {
                resultAdapter?.updateData(it)
                if(iv_search_clear.isVisible && it.isEmpty())
                    requestEtFocus(et_search)
            }
        })
        viewModel.suggestions.observe(this as LifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                if(et_search.adapter == null)
                    et_search.setAdapter(
                        ArrayAdapter<String>(context!!,
                                             android.R.layout.simple_dropdown_item_1line, it))
                else
                    (et_search.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            }
        })
        viewModel.retryResId.observe(this as LifecycleOwner, Observer {
            when(it) {
                R.string.retry_load_drama -> showReload(it)
                R.string.retry_search_drama -> showResearch(it)
            }
        })
    }

    private fun gotoDetail(drama: Drama) {
        KeyboardHelper.hide(activity!!)
        if(activity is MainActivity)
            (activity as MainActivity).gotoDetail(drama)
    }

    private fun getDramas() {
        val connManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val request = NetworkRequest.Builder().build()
        connManager?.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if(!isInitData) {
                    isInitData = true
                    viewModel.getDramas()
                    connManager.unregisterNetworkCallback(this)
                }
            }
        })
        if(!isInitData && connManager?.activeNetworkInfo == null)
            viewModel.getLocalDrams()
    }

    private fun requestEtFocus(editText: EditText?) {
        if (editText!= null && !editText.isFocused)
            editText.requestFocus()
        KeyboardHelper.show(activity!!)
    }

    private fun clearEtFocus(editText: EditText?) {
        KeyboardHelper.hide(activity!!)
        if (editText!= null && editText.isFocused)
            editText.clearFocus()
    }

    private fun showResearch(@StringRes resId: Int) {
        if(activity is MainActivity)
            (activity as MainActivity).showSnackBarWithCheck(main, resId) {
                viewModel.getDramas()
            }
    }

    private fun showReload(@StringRes resId: Int) {
        if(activity is MainActivity)
            (activity as MainActivity).showSnackBarWithCheck(main, resId) {
                searchByKeyword()
            }
    }
}
