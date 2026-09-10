package com.udacity.asteroidradar.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
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
import com.udacity.asteroidradar.data.source.AsteroidRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepositoryImpl(
    private val remoteDataSource: AsteroidRemoteDataSource,
    private val database: AsteroidDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AsteroidRepository{

    private val _statusStateFlow = MutableStateFlow<AsteroidApiStatus>(AsteroidApiStatus.LOADING)
    override val statusStateFlow: StateFlow<AsteroidApiStatus> = _statusStateFlow
    val pagingConfig = PagingConfig(pageSize = 10, prefetchDistance = 5, enablePlaceholders = false)

    override fun getAsteroids(filter: AsteroidApiFilter): Flow<PagingData<AsteroidModel>> {
        val (startDate, endDate) = getDateRange(filter)

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                database.asteroidDao.getAsteroidsList(startDate, endDate)
            }
        ).flow
    }

   override suspend fun refreshAsteroids(filter: AsteroidApiFilter) {
        if (filter == AsteroidApiFilter.SHOW_SAVED || !isNetworkConnected()) return

        withContext(ioDispatcher) {
            _statusStateFlow.value = AsteroidApiStatus.LOADING
            try {
                val (startDate, endDate) = getDateRange(filter)

                val response = remoteDataSource.getAsteroids(startDate, endDate)
                database.asteroidDao.insertAll(response)
                _statusStateFlow.value = AsteroidApiStatus.DONE

            } catch (e: Exception) {
                ensureActive()
                _statusStateFlow.value = AsteroidApiStatus.ERROR
                Timber.d("Exception: $e")
            }
        }
    }

    override suspend fun getAsteroidById(id: Long): Result<AsteroidModel> {
        return withContext(ioDispatcher) {
            try {
                val entity = database.asteroidDao.getAsteroidById(id)
                if (entity != null) {
                    Result.success(entity)
                } else {
                    ensureActive()
                    Result.failure(Exception("Asteroid not found"))
                }
            } catch (e: Exception) {
                ensureActive()
                Timber.e(e, "Error getting asteroid by id")
                Result.failure(e)
            }
        }
    }


   override fun getImageOfDay(): Flow<ImageOfTodayModel?> {
        return database.imageOfTodayDao.getImageOfToday(getTodayDate())

    }


    override suspend fun refreshImageOfDay() {
        withContext(ioDispatcher) {
            if (!isNetworkConnected()) {
                Timber.d("No network connection, skipping image refresh")
                return@withContext
            }

            _statusStateFlow.value = AsteroidApiStatus.LOADING


            try {
                val dto = remoteDataSource.getImageOfDay()
                database.imageOfTodayDao.insertImageOfToday(dto)
                Timber.d("Successfully refreshed image of day")
                _statusStateFlow.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                ensureActive()
                Timber.e(e, "Error refreshing image of day")
                _statusStateFlow.value = AsteroidApiStatus.ERROR
            }
        }
    }


    private fun getDateRange(filter: AsteroidApiFilter): Pair<String, String> {
        return when (filter) {
            AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
            AsteroidApiFilter.SHOW_WEEK -> getTodayDate() to getEndDate()
            AsteroidApiFilter.SHOW_SAVED -> getTodayDate() to getEndDate()
        }
    }

}