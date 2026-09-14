package com.udacity.asteroidradar.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.AsteroidApiStatus
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.isNetworkConnected
import com.udacity.asteroidradar.data.mapper.toDomain
import com.udacity.asteroidradar.data.mapper.toEntity
import com.udacity.asteroidradar.data.source.AsteroidLocalDataSource
import com.udacity.asteroidradar.data.source.AsteroidRemoteDataSource
import com.udacity.asteroidradar.domain.AsteroidModel
import com.udacity.asteroidradar.domain.ImageOfDayModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class AsteroidRepositoryImpl(
    private val remoteDataSource: AsteroidRemoteDataSource,
    private val localDataSource: AsteroidLocalDataSource,
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
                localDataSource.getAsteroidsPagingSource(startDate, endDate)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun refreshAsteroids(filter: AsteroidApiFilter) {
        if (filter == AsteroidApiFilter.SHOW_SAVED || !isNetworkConnected()) return

        withContext(ioDispatcher) {
            _statusStateFlow.value = AsteroidApiStatus.LOADING

            try {
                val (startDate, endDate) = getDateRange(filter)
                val asteroids = remoteDataSource.getAsteroids(startDate, endDate)
                val entities = asteroids.map { it.toEntity() }
                localDataSource.insertAsteroids(entities)
                _statusStateFlow.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                ensureActive()
                _statusStateFlow.value = AsteroidApiStatus.ERROR
                Timber.e(e, "Error refreshing asteroids")
            }
        }
    }

    override suspend fun getAsteroidById(id: Long): Result<AsteroidModel> {
        return withContext(ioDispatcher) {
            try {
                val entity = localDataSource.getAsteroidById(id)
                if (entity != null) {
                    Result.success(entity.toDomain())
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


    override fun getImageOfDay(): Flow<ImageOfDayModel?> {
        return localDataSource.getImageOfDay(getTodayDate())
            .map { it?.toDomain() }
    }


    override suspend fun refreshImageOfDay() {
        if (!isNetworkConnected()) {
            Timber.d("No network connection, skipping image refresh")
            return
        }

        withContext(ioDispatcher) {

            _statusStateFlow.value = AsteroidApiStatus.LOADING

            try {
                val dto = remoteDataSource.getImageOfDay()
                val entity = dto.toEntity(getTodayDate())
                localDataSource.insertImageOfDay(entity)
                _statusStateFlow.value = AsteroidApiStatus.DONE
                Timber.d("Successfully refreshed image of day")
            } catch (e: Exception) {
                ensureActive()
                _statusStateFlow.value = AsteroidApiStatus.ERROR
                Timber.e(e, "Error refreshing image of day")
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