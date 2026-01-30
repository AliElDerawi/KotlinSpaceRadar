package com.udacity.asteroidradar.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for Asteroid
 * This model is only used for database persistence
 */
@Entity(tableName = "asteroid_data")
data class AsteroidEntity(
    @PrimaryKey val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

