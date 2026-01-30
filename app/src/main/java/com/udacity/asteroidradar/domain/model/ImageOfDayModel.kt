package com.udacity.asteroidradar.domain.model

/**
 * Domain model for Image of the Day - Pure Kotlin, no framework dependencies
 * This model is used across the presentation layer (ViewModels and UI)
 */
data class ImageOfDayModel(
    val title: String,
    val url: String,
    val mediaType: String,
    val date: String
)

