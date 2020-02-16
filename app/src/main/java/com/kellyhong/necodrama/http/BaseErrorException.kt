package com.kellyhong.necodrama.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kellyhong.necodrama.R

data class BaseErrorException(
        @SerializedName("errorCode")
        @Expose var errorType: String,
        @Expose var errorMsg: String = "Error massage from call",
        @Expose var errorTip: Int = R.string.response_err_unknown): Throwable(errorMsg) {

    companion object {
        @JvmField
        val CancelCall = BaseErrorException("ExAPP00001", "Previous call is canceled.", R.string.response_err_cancel)
        @JvmField
        val InternetError = BaseErrorException("ExAPP00002", "Internet Error", R.string.response_err_network)
        @JvmField
        val OtherError = BaseErrorException("ExAPP00003", "Unknown Error", R.string.response_err_unknown)
        @JvmField
        val NullResponse = BaseErrorException("ExAPP00005", "Required response is null", R.string.response_err_unknown)
        @JvmField
        val ServerError = BaseErrorException("ExAPP00006", "Server internal error", R.string.response_err_unknown)
    }
}