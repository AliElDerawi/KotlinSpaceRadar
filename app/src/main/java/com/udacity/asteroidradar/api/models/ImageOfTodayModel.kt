package com.udacity.asteroidradar.api.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "image_of_day_data")
data class ImageOfTodayModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,

    @Json(name = "media_type") var mediaType: String = "",

    @Json(name = "title") var title: String = "",

    @Json(name = "url") var url: String = "",

    @Json(name = "date") var date: String = ""

) {
}

