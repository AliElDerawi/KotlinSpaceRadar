package com.udacity.asteroidradar.domain.model

/**
 * Domain model for Asteroid - Pure Kotlin, no framework dependencies
 * This model is used across the presentation layer (ViewModels and UI)
 */
data class AsteroidModel(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

