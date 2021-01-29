package com.fiesta.detector.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "poi_table")
@Parcelize
data class Poi (
    val name: String,
    val description: String,
    val location: LatLng,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey( autoGenerate = true) val id: Int = 0
    ) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}