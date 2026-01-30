package com.udacity.asteroidradar.data.source.remote

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.source.remote.dto.ImageOfDayDto
import com.udacity.asteroidradar.domain.model.AsteroidModel
import org.json.JSONObject
import timber.log.Timber

/**
 * Interface for remote data source
 * Defines contract for fetching data from API
 */
interface AsteroidRemoteDataSource {
    suspend fun getAsteroids(startDate: String, endDate: String): List<AsteroidModel>
    suspend fun getImageOfDay(): ImageOfDayDto
}

/**
 * Implementation of remote data source
 * Handles all API calls
 */
class AsteroidRemoteDataSourceImpl : AsteroidRemoteDataSource {
    
    override suspend fun getAsteroids(startDate: String, endDate: String): List<AsteroidModel> {
        return try {
            val response = AsteroidApi.retrofitService.getAsteroid(startDate, endDate)
            val jsonObject = JSONObject(response)
            parseAsteroidsJsonResult(jsonObject)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching asteroids from API")
            emptyList()
        }
    }
    
    override suspend fun getImageOfDay(): ImageOfDayDto {
        return AsteroidApi.retrofitService.getImageOfTheDay()
    }
}

