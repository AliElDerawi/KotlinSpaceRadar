package com.udacity.asteroidradar.api.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.GenericModelCallBack
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "asteroid_data")
@Parcelize
data class AsteroidModel(
    @PrimaryKey
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable {
    companion object {
        fun getAsteroidModelCallback(): GenericModelCallBack<AsteroidModel> {
            return GenericModelCallBack(
                _areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                _areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        }
    }
}