package com.kellyhong.necodrama.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kellyhong.necodrama.R
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.http.BaseErrorException
import com.kellyhong.necodrama.http.ErrorConsumer
import com.kellyhong.necodrama.model.MainDataSource
import io.reactivex.functions.Consumer

@SuppressLint("CheckResult")
class MainViewModel(private val repository: MainDataSource) : ViewModel() {

    val toastResId = MutableLiveData<Int>()
    val retryResId = MutableLiveData<Int>()
    val loadingEvent = MutableLiveData<Int>()
    val dramas = MutableLiveData<MutableList<Drama>>()
    val suggestions = MutableLiveData<MutableList<String>>(arrayListOf())

    fun getLocalDrams() {
        loadingEvent.postValue(R.string.loading)
        repository.getAllDramas()
            .doFinally { loadingEvent.postValue(-1) }
            .subscribe( Consumer { re ->
                updateSuggestions(re)
                dramas.postValue(re)
            }, object : ErrorConsumer() {
                override fun normalErr(error: BaseErrorException) {
                    toastResId.postValue(error.errorTip)
                }
            })
    }

    fun getDramas() {
        loadingEvent.postValue(R.string.loading)
        repository.updateAndGetAllDramas()
            .doFinally { loadingEvent.postValue(-1) }
            .subscribe( Consumer { re ->
                updateSuggestions(re)
                dramas.postValue(re)
            }, object : ErrorConsumer() {
                override fun normalErr(error: BaseErrorException) {
                    if(error.errorTip == BaseErrorException.OtherError.errorTip)
                        retryResId.postValue(R.string.retry_load_drama)
                    else
                        toastResId.postValue(error.errorTip)
                }
            })
    }

    private fun updateSuggestions(dramas: List<Drama>) {
        if(suggestions.value != null) {
            suggestions.value!!.clear()
            dramas.forEach {
                suggestions.value!!.add(it.name)
            }
            suggestions.postValue(suggestions.value)
        }
    }

    fun searchDrama(keyword: String) {
        repository.searchDramas(keyword)
            .subscribe( Consumer { re ->
                dramas.postValue(re)
            }, object : ErrorConsumer() {
                override fun normalErr(error: BaseErrorException) {
                    if(error.errorTip == BaseErrorException.OtherError.errorTip)
                        retryResId.postValue(R.string.retry_search_drama)
                    else
                        toastResId.postValue(error.errorTip)
                }
            })
    }
}
