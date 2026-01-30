package com.udacity.asteroidradar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.isNetworkConnected
import com.udacity.asteroidradar.data.mapper.toDomain
import com.udacity.asteroidradar.data.mapper.toEntity
import com.udacity.asteroidradar.data.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.data.source.remote.AsteroidRemoteDataSource
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of AsteroidRepository interface
 * Coordinates between remote and local data sources
 * Implements offline-first strategy
 */
class AsteroidRepositoryImpl(
    private val remoteDataSource: AsteroidRemoteDataSource,
    private val localDataSource: AsteroidLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AsteroidRepository {

    private val pagingConfig = PagingConfig(
        pageSize = 10,
        prefetchDistance = 5,
        enablePlaceholders = false
    )

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

    override suspend fun getAsteroidById(id: Long): Result<AsteroidModel> {
        return withContext(ioDispatcher) {
            try {
                val entity = localDataSource.getAsteroidById(id)
                if (entity != null) {
                    Result.success(entity.toDomain())
                } else {
                    Result.failure(Exception("Asteroid not found"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting asteroid by id")
                Result.failure(e)
            }
        }
    }

    override fun getImageOfDay(): Flow<ImageOfDayModel?> {
        return localDataSource.getImageOfDay(getTodayDate())
            .map { it?.toDomain() }
    }

    override suspend fun refreshAsteroids(filter: AsteroidApiFilter) {
        withContext(ioDispatcher) {
            if (!isNetworkConnected()) {
                Timber.d("No network connection, skipping refresh")
                return@withContext
            }

            try {
                val (startDate, endDate) = getDateRange(filter)
                val asteroids = remoteDataSource.getAsteroids(startDate, endDate)
                val entities = asteroids.map { it.toEntity() }
                localDataSource.insertAsteroids(entities)
                Timber.d("Successfully refreshed ${entities.size} asteroids")
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing asteroids")
            }
        }
    }

    override suspend fun refreshImageOfDay() {
        withContext(ioDispatcher) {
            if (!isNetworkConnected()) {
                Timber.d("No network connection, skipping image refresh")
                return@withContext
            }

            try {
                val dto = remoteDataSource.getImageOfDay()
                val entity = dto.toEntity(getTodayDate())
                localDataSource.insertImageOfDay(entity)
                Timber.d("Successfully refreshed image of day")
            } catch (e: Exception) {
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

