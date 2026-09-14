package com.udacity.asteroidradar.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for Image of the Day
 * This model is only used for database persistence
 */
@Entity(tableName = "image_of_day_data")
data class ImageOfDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val mediaType: String,
    val title: String,
    val url: String,
    val date: String,
    val creationDate: String
)

