package com.udacity.asteroidradar.api.models

import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class ImageOfDayDto(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @Json(name = "media_type") var mediaType: String = "",
    @Json(name = "title") var title: String = "",
    @Json(name = "url") var url: String = "",
    @Json(name = "date") var date: String = "",
    var creationDate: String = ""
) {}

