package com.udacity.asteroidradar.api.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "image_of_day_data")
class ImageOfTodayModel {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @Json(name = "media_type")
    var mediaType: String = ""

    @Json(name = "title")
    var title: String = ""

    @Json(name = "url")
    var url: String = ""

    @Json(name = "date")
    var date: String = ""

    @Ignore
    constructor() {

    }

    @Ignore
    constructor(mediaType: String, title: String, url: String, date: String) {
        this.mediaType = mediaType
        this.title = title
        this.url = url
        this.date = date
    }

    constructor(id: Long, mediaType: String, title: String, url: String, date: String) {
        this.id = id
        this.mediaType = mediaType
        this.title = title
        this.url = url
        this.date = date
    }


}