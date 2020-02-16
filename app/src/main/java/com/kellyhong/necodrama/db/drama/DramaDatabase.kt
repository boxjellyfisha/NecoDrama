package com.kellyhong.necodrama.db.drama

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kellyhong.necodrama.util.DateTypeConverters

@Database(entities = [Drama::class, DramaFts::class], version = 2, exportSchema = false)
@TypeConverters(DateTypeConverters::class)
abstract class DramaDatabase: RoomDatabase() {

	abstract fun dramaDao(): DramaDao

	companion object {
		private const val DB_NAME = "regular.db"

		@Volatile
		private var INSTANCE: DramaDatabase? = null

		@JvmStatic
		fun getInstance(context: Context): DramaDatabase =
				INSTANCE ?: synchronized(this) {
					INSTANCE
							?: buildDatabase(context).also {
								INSTANCE = it
							}
				}

		private fun buildDatabase(context: Context): DramaDatabase {
			return Room.databaseBuilder(context.applicationContext,
					DramaDatabase::class.java, DB_NAME
			).addMigrations(MIGRATION_1_2).build()
		}

		@JvmField
		val MIGRATION_1_2: Migration = object : Migration(1, 2) {
			override fun migrate(database: SupportSQLiteDatabase) {
				database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `DramaFts` USING FTS4(`name`, content=`Drama`)")
				database.execSQL("INSERT INTO DramaFts(DramaFts) VALUES ('rebuild')")
			}
		}
	}
}