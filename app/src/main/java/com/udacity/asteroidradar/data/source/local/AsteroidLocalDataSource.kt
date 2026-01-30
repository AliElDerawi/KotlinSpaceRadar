package com.udacity.asteroidradar.data.source.local

import androidx.paging.PagingSource
import com.udacity.asteroidradar.data.database.AsteroidDao
import com.udacity.asteroidradar.data.database.ImageOfTodayDao
import com.udacity.asteroidradar.data.source.local.entity.AsteroidEntity
import com.udacity.asteroidradar.data.source.local.entity.ImageOfDayEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface for local data source
 * Defines contract for database operations
 */
interface AsteroidLocalDataSource {
    fun getAsteroidsPagingSource(startDate: String, endDate: String): PagingSource<Int, AsteroidEntity>
    suspend fun getAsteroidById(id: Long): AsteroidEntity?
    suspend fun insertAsteroids(asteroids: List<AsteroidEntity>)
    fun getImageOfDay(currentDate: String): Flow<ImageOfDayEntity?>
    suspend fun insertImageOfDay(image: ImageOfDayEntity)
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
    ): PagingSource<Int, AsteroidEntity> {
        return asteroidDao.getAsteroidsList(startDate, endDate)
    }
    
    override suspend fun getAsteroidById(id: Long): AsteroidEntity? {
        return asteroidDao.getAsteroidById(id)
    }
    
    override suspend fun insertAsteroids(asteroids: List<AsteroidEntity>) {
        asteroidDao.insertAll(*asteroids.toTypedArray())
    }
    
    override fun getImageOfDay(currentDate: String): Flow<ImageOfDayEntity?> {
        return imageOfDayDao.getImageOfToday(currentDate)
    }
    
    override suspend fun insertImageOfDay(image: ImageOfDayEntity) {
        imageOfDayDao.insertImageOfToday(image)
    }
}

