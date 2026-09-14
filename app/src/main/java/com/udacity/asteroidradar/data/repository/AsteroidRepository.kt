package com.udacity.asteroidradar.data.repository


import androidx.paging.PagingData
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.AsteroidApiStatus
import com.udacity.asteroidradar.domain.AsteroidModel
import com.udacity.asteroidradar.domain.ImageOfDayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AsteroidRepository{

    val statusStateFlow: StateFlow<AsteroidApiStatus>

    fun getAsteroids(filter: AsteroidApiFilter): Flow<PagingData<AsteroidModel>>

    /**
     * Get a specific asteroid by ID
     * @param id The asteroid ID
     * @return Result containing the Asteroid or error
     */
    suspend fun getAsteroidById(id: Long): Result<AsteroidModel>

    /**
     * Get the image of the day
     * @return Flow of ImageOfTodayModel or null if not available
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