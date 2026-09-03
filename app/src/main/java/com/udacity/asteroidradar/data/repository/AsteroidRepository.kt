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

class AsteroidRepository(
    private val database: AsteroidDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val _statusStateFlow = MutableStateFlow<AsteroidApiStatus>(AsteroidApiStatus.DONE)
    val statusStateFlow: StateFlow<AsteroidApiStatus> = _statusStateFlow
    val pagingConfig = PagingConfig(pageSize = 10, prefetchDistance = 5, enablePlaceholders = false)

    fun getAsteroidsFromDataBaseFlow(filter: AsteroidApiFilter): Flow<PagingData<AsteroidModel>> {
        val (startDate, endDate) = when (filter) {
            AsteroidApiFilter.SHOW_WEEK -> getTodayDate() to getEndDate()
            AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
            AsteroidApiFilter.SHOW_SAVED -> getTodayDate() to getEndDate()
        }

        return Pager(pagingConfig) {
            database.asteroidDao.getAsteroidsList(startDate, endDate)
        }.flow
    }

    suspend fun refreshAsteroids(filter: AsteroidApiFilter) {
        if (filter == AsteroidApiFilter.SHOW_SAVED || !isNetworkConnected()) return

        withContext(ioDispatcher) {
            _statusStateFlow.value = AsteroidApiStatus.LOADING
            try {
                val (startDate, endDate) = when (filter) {
                    AsteroidApiFilter.SHOW_WEEK -> getTodayDate() to getEndDate()
                    AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
                }

                val response = AsteroidApi.retrofitService.getAsteroid(startDate, endDate)
                val jsonObject = JSONObject(response)
                val asteroids = parseAsteroidsJsonResult(jsonObject)

                database.asteroidDao.insertAll(*asteroids.toTypedArray())
                _statusStateFlow.value = AsteroidApiStatus.DONE

            } catch (e: Exception) {
                ensureActive()
                _statusStateFlow.value = AsteroidApiStatus.ERROR
                Timber.d("Exception: $e")
            }
        }
    }


    fun getImageOfTodayFlow(): Flow<ImageOfTodayModel?> {
        return flow {
            if (isNetworkConnected()) {
                try {
                    val response = AsteroidApi.retrofitService.getImageOfTheDay()
                    response.creationDate = getTodayDate()
                    database.imageOfTodayDao.insertImageOfToday(response)
                } catch (e: Exception) {
                    Timber.d("Network Exception: $e")
                }
            }
            val localImage = database.imageOfTodayDao.getImageOfToday(getTodayDate()).first()
            emit(localImage)

        }.flowOn(ioDispatcher)
    }

}