package com.udacity.asteroidradar.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.AsteroidApiStatus
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.isNetworkConnected
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.database.AsteroidDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

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
    fun getImageOfDay(): Flow<ImageOfTodayModel?>

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