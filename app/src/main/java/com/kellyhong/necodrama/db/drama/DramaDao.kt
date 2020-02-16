package com.kellyhong.necodrama.db.drama

import androidx.room.*
import com.kellyhong.necodrama.db.drama.Drama
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DramaDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(drama: List<Drama>)

	@Delete
	fun delete(drama: Drama)

	@Query("SELECT DISTINCT * FROM Drama")
	fun getDramaList(): Flowable<List<Drama>>

	@Transaction
	@Query("SELECT * FROM Drama JOIN DramaFts ON (Drama.id = DramaFts.docid) WHERE DramaFts MATCH :term")
	fun search(term: String): Flowable<List<Drama>>

	@Transaction
	@Query("SELECT * FROM DramaFts WHERE DramaFts MATCH :term")
	fun searchName(term: String): Flowable<List<String>>

	@Transaction
	@Query("SELECT * FROM Drama WHERE Drama.name LIKE :term")
	fun searchLike(term: String): Flowable<List<Drama>>

	@Query("DELETE FROM Drama")
	fun deleteAll(): Single<Int>
}