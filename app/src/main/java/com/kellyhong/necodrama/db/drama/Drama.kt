package com.kellyhong.necodrama.db.drama


import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.math.BigDecimal

@Entity(tableName = "Drama")
data class Drama(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @Expose
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    val createdAt: String = "",

    @ColumnInfo(name = "joined_date")
    var joinedDate: OffsetDateTime? = null,

    @ColumnInfo(name = "drama_id")
    @Expose
    @SerializedName("drama_id")
    val dramaId: String = "",

    @Expose
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = "",

    @Expose
    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    val rating: Double = 0.0,

    @Expose
    @ColumnInfo(name = "thumb")
    @SerializedName("thumb")
    val thumb: String = "",

    @Expose
    @ColumnInfo(name = "total_views")
    @SerializedName("total_views")
    val totalViews: Int = 0
) : Parcelable {
    fun createDateTime(): Drama {
        if (!createdAt.isNullOrEmpty())
            joinedDate = OffsetDateTime.parse(createdAt.replace("Z", "+08:00"))
        return this
    }

    fun getDisplayDate() = joinedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    fun getDisplayTime() = joinedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                     ?.replace("T", " ")
    fun getRate(): BigDecimal = rating.toBigDecimal().setScale(1, 4)

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()?:"",
        source.readSerializable() as OffsetDateTime?,
        source.readString()?:"",
        source.readString()?:"",
        source.readDouble(),
        source.readString()?:"",
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(createdAt)
        writeSerializable(joinedDate)
        writeString(dramaId)
        writeString(name)
        writeDouble(rating)
        writeString(thumb)
        writeInt(totalViews)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Drama> = object : Parcelable.Creator<Drama> {
            override fun createFromParcel(source: Parcel): Drama = Drama(source)
            override fun newArray(size: Int): Array<Drama?> = arrayOfNulls(size)
        }
    }
}