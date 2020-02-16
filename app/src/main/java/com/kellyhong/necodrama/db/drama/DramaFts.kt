package com.kellyhong.necodrama.db.drama


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.google.gson.annotations.SerializedName

@Fts4(contentEntity = Drama::class)
@Entity(tableName = "DramaFts")
data class DramaFts(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = ""
)