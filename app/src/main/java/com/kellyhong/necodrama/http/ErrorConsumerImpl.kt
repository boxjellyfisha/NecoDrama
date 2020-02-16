package com.kellyhong.necodrama.http

import androidx.lifecycle.MutableLiveData
import com.kellyhong.necodrama.R
import java.lang.ref.WeakReference

class ErrorConsumerImpl(toastEvent: MutableLiveData<Int>? = null): ErrorConsumer() {
    private val weakToast: WeakReference<MutableLiveData<Int>?> = WeakReference(toastEvent)
    override fun normalErr(error: BaseErrorException) {
        if(error.errorTip == 0)
            weakToast.get()?.postValue(R.string.response_err_unknown)
        else
            weakToast.get()?.postValue(error.errorTip)
        weakToast.clear()
    }
}