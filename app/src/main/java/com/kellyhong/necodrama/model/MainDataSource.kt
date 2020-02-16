package com.kellyhong.necodrama.model

import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.http.response.DramaListResponse
import io.reactivex.Flowable
import io.reactivex.Single

interface MainDataSource {

    fun getAllDramas(): Single<MutableList<Drama>>

    fun updateAndGetAllDramas(): Single<MutableList<Drama>>

    fun searchDramaNames(keyword: String): Single<MutableList<Drama>>
    fun searchDramas(keyword: String): Single<MutableList<Drama>>

    interface Remote {
        fun getDramaList(): Single<DramaListResponse>
    }

    interface Local{
        fun searchName(term: String): Flowable<List<String>>
        fun searchLike(term: String): Flowable<List<Drama>>
        fun search(term: String): Flowable<List<Drama>>

        fun getDramaList(): Flowable<List<Drama>>

        fun deleteDramas(): Single<Int>

        fun insertDramas(drama: List<Drama>)
    }
}