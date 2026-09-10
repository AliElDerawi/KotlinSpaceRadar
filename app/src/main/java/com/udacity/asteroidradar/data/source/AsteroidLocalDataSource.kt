package com.udacity.asteroidradar.data.source

import androidx.paging.PagingSource
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.database.AsteroidDao
import com.udacity.asteroidradar.data.database.ImageOfTodayDao
import kotlinx.coroutines.flow.Flow

/**
 * Interface for local data source
 * Defines contract for database operations
 */
interface AsteroidLocalDataSource {
    fun getAsteroidsPagingSource(
        startDate: String,
        endDate: String
    ): PagingSource<Int, AsteroidModel>

    suspend fun getAsteroidById(id: Long): AsteroidModel?
    suspend fun insertAsteroids(asteroids: List<AsteroidModel>)
    fun getImageOfDay(currentDate: String): Flow<ImageOfTodayModel?>
    suspend fun insertImageOfDay(image: ImageOfTodayModel)
}

/**
 * Implementation of local data source
 * Handles all database operations
 */
class AsteroidLocalDataSourceImpl(
    private val asteroidDao: AsteroidDao,
    private val imageOfDayDao: ImageOfTodayDao
) : AsteroidLocalDataSource {

    override fun getAsteroidsPagingSource(
        startDate: String,
        endDate: String
    ): PagingSource<Int, AsteroidModel> {
        return asteroidDao.getAsteroidsList(startDate, endDate)
    }

    override suspend fun getAsteroidById(id: Long): AsteroidModel? {
        return asteroidDao.getAsteroidById(id)
    }

    override suspend fun insertAsteroids(asteroids: List<AsteroidModel>) {
        asteroidDao.insertAll(asteroids)
    }

    override fun getImageOfDay(currentDate: String): Flow<ImageOfTodayModel?> {
        return imageOfDayDao.getImageOfToday(currentDate)
    }

    override suspend fun insertImageOfDay(image: ImageOfTodayModel) {
        imageOfDayDao.insertImageOfToday(image)
    }
}

