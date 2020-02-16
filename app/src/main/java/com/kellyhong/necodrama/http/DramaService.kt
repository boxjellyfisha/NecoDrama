package com.kellyhong.necodrama.http

import com.kellyhong.necodrama.http.response.DramaListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface DramaService {
    @GET("interview/dramas-sample.json")
    fun getDramaList(): Single<DramaListResponse>
}