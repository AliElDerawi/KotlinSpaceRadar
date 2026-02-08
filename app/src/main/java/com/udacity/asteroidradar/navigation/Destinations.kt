package com.udacity.asteroidradar.navigation

import com.udacity.asteroidradar.R
import kotlinx.serialization.Serializable

/**
 * Centralized navigation destinations for the app.
 * 
 * All navigation destinations are defined here to:
 * - Avoid circular dependencies between feature modules
 * - Provide a single source of truth for navigation routes
 * - Keep screen composables free of navigation concerns
 * - Make it easy to see all routes at a glance
 */

@Serializable
object HomeDestination {
    val titleRes = R.string.app_name
}

@Serializable
data class AsteroidDetailDestination(
    val asteroidId: Long
) {
    companion object {
        val titleRes = R.string.text_asteroid_detail
    }
}
