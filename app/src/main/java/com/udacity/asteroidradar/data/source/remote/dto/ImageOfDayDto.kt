package com.udacity.asteroidradar.data.source.remote.dto

import com.squareup.moshi.Json

/**
 * Data Transfer Object for Image of the Day from API
 * This model is only used for API responses
 */
data class ImageOfDayDto(
    @field:Json(name = "media_type") val mediaType: String = "",
    @field:Json(name = "title") val title: String = "",
    @field:Json(name = "url") val url: String = "",
    @field:Json(name = "date") val date: String = ""
)

