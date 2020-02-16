package com.kellyhong.necodrama.model

import com.kellyhong.necodrama.db.AppPreference
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.db.drama.DramaDatabase
import io.reactivex.Flowable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainLocal(dramaDB: DramaDatabase): MainDataSource.Local, KoinComponent {

    private val dramaDao = dramaDB.dramaDao()
    private val appPreference: AppPreference by inject()

    override fun insertDramas(dramas: List<Drama>) {
        dramaDao.insertAll(dramas)
    }

    override fun deleteDramas(): Single<Int> {
        return dramaDao.deleteAll()
    }

    override fun searchName(term: String): Flowable<List<String>> {
        val newTerm = "*$term*"
        return dramaDao.searchName(newTerm)
    }

    override fun searchLike(term: String): Flowable<List<Drama>> {
        val newTerm = "%$term%"
        return dramaDao.searchLike(newTerm)
    }

    override fun search(term: String): Flowable<List<Drama>> {
        val newTerm = "*$term*"
        return dramaDao.search(newTerm)
    }

    override fun getDramaList(): Flowable<List<Drama>> {
        return dramaDao.getDramaList()
    }
}