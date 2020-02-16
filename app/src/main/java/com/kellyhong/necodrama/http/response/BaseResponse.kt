package com.kellyhong.necodrama.http.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @Expose
    @SerializedName("errorCode")
    val errorCode: String? = null
)