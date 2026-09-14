package com.udacity.asteroidradar.domain

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.GenericModelCallBack
import kotlinx.parcelize.Parcelize

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