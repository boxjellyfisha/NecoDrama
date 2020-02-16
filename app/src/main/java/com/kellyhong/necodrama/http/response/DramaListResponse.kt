package com.kellyhong.necodrama.http.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kellyhong.necodrama.db.drama.Drama

data class DramaListResponse(
    @Expose
    @SerializedName("data")
    val `data`: MutableList<Drama>? = null
)