package com.kellyhong.necodrama.model

import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.http.catchError
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MainRepository(
    private val local: MainDataSource.Local,
    private val remote: MainDataSource.Remote
) : MainDataSource {

    override fun getAllDramas(): Single<MutableList<Drama>> {
        return local.getDramaList().firstOrError().map {
            ArrayList<Drama>(it)
        }
    }

    override fun updateAndGetAllDramas(): Single<MutableList<Drama>> {
        return local.deleteDramas()
            .subscribeOn(Schedulers.io())
            .flatMap {
                remote.getDramaList()
            }.catchError()
            .map { response ->
                val list = response.data
                if (!list.isNullOrEmpty()) {
                    list.forEach { it.createDateTime() }
                    local.insertDramas(list)
                    list
                } else
                    arrayListOf()
            }
    }

    override fun searchDramaNames(keyword: String): Single<MutableList<Drama>> {
        return local.searchName(keyword).firstOrError()
            .subscribeOn(Schedulers.io())
            .map { names ->
                val list = arrayListOf<Drama>()
                names.forEach {
                    list.add(Drama(0, name = it))
                }
                list
            }
    }

    override fun searchDramas(keyword: String): Single<MutableList<Drama>> {
        return local.search(keyword)
            .subscribeOn(Schedulers.io())
            .firstOrError()
            .flatMap {
                if(it.isEmpty())
                    local.searchLike(keyword).firstOrError().map { ArrayList<Drama>(it) }
                else
                    Single.just(ArrayList(it))
            }
    }
}