package com.udacity.asteroidradar.domain.model

/**
 * Domain model for filtering asteroids
 * Replaces AsteroidApiFilter to decouple domain from API layer
 */
enum class AsteroidApiFilter {
    SHOW_TODAY,
    SHOW_WEEK,
    SHOW_SAVED
}

