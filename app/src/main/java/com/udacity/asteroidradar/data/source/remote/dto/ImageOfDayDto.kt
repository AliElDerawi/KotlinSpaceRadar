package com.udacity.asteroidradar.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Data Transfer Object for Image of the Day from API
 * This model is only used for API responses
 */
@Serializable // علامة أساسية ليعرف المترجم أن هذا الكلاس قابل للتحليل
data class ImageOfDayDto(
    @SerialName("media_type") val mediaType: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("date") val date: String = ""
)