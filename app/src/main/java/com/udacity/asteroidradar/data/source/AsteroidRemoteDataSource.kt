package com.udacity.asteroidradar.data.source

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import timber.log.Timber

/**
 * Interface for remote data source
 * Defines contract for fetching data from API
 */
interface AsteroidRemoteDataSource {
    suspend fun getAsteroids(startDate: String, endDate: String): List<AsteroidModel>
    suspend fun getImageOfDay(): ImageOfTodayModel
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
            Timber.d("getAsteroids: Fetched ${jsonObject.length()} asteroids from API")
            parseAsteroidsJsonResult(jsonObject)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching asteroids from API")
            emptyList()
        }
    }
    
    override suspend fun getImageOfDay(): ImageOfTodayModel {
        return try {
            val imageOfToday = AsteroidApi.retrofitService.getImageOfTheDay()
            Timber.d("getImageOfDay: Fetched image of day from API")
            imageOfToday
        } catch (e: Exception) {
            Timber.e(e, "Error fetching image of day from API")
            throw e
        }
    }
}

