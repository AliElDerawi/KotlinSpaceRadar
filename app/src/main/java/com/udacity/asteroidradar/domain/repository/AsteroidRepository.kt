package com.udacity.asteroidradar.domain.repository

import androidx.paging.PagingData
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface in domain layer
 * Defines the contract for data operations without implementation details
 * This allows the domain layer to be independent of data layer implementation
 */
interface AsteroidRepository {
    /**
     * Get asteroids with pagination based on filter
     * @param filter The filter to apply (TODAY, WEEK, SAVED)
     * @return Flow of PagingData containing Asteroid domain models
     */
    fun getAsteroids(filter: AsteroidApiFilter): Flow<PagingData<AsteroidModel>>
    
    /**
     * Get a specific asteroid by ID
     * @param id The asteroid ID
     * @return Result containing the Asteroid or error
     */
    suspend fun getAsteroidById(id: Long): Result<AsteroidModel>
    
    /**
     * Get the image of the day
     * @return Flow of ImageOfDay or null if not available
     */
    fun getImageOfDay(): Flow<ImageOfDayModel?>
    
    /**
     * Refresh asteroids from remote source
     * @param filter The filter to apply
     */
    suspend fun refreshAsteroids(filter: AsteroidApiFilter)
    
    /**
     * Refresh image of the day from remote source
     */
    suspend fun refreshImageOfDay()
}

