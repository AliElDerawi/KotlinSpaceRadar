package com.udacity.asteroidradar.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.isNetworkConnected
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.database.AsteroidDatabase
import com.udacity.asteroidradar.util.ApiPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    val pagingConfig = PagingConfig(pageSize = 10, prefetchDistance = 5, enablePlaceholders = false)

    suspend fun refreshAsteroids(
        filter: AsteroidApiFilter
    ): Result<Flow<PagingData<AsteroidModel>>> {
        return withContext(ioDispatcher) {

            if (!isNetworkConnected()) {
                getAsteroidListFromDataBase(filter)
            }

            try {
                if (filter == AsteroidApiFilter.SHOW_SAVED) {
                    val result = Pager(pagingConfig) {
                        database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                    }.flow
                    Result.success(result)
                } else {
                    val (startDate, endDate) = when (filter) {
                        AsteroidApiFilter.SHOW_WEEK -> getTodayDate() to getEndDate()
                        AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
                        else -> getTodayDate() to getTodayDate()
                    }

                    val response = AsteroidApi.retrofitService.getAsteroid(startDate, endDate)
                    val jsonObject = JSONObject(response)
                    val asteroids = parseAsteroidsJsonResult(jsonObject)

                    database.asteroidDao.insertAll(*asteroids.toTypedArray())

                    val result = Pager(pagingConfig) { ApiPagingSource(asteroids) }.flow
                    Result.success(result)
                }
            } catch (e: Exception) {
                ensureActive()
                Timber.d("Exception: $e")
                getAsteroidListFromDataBase(filter)
            }
        }
    }



    private suspend fun getAsteroidListFromDataBase(filter: AsteroidApiFilter): Result<Flow<PagingData<AsteroidModel>>> {
        return withContext(ioDispatcher) {
            val (startDate, endDate) = when (filter) {
                AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
                else -> getTodayDate() to getEndDate()
            }

            val result = Pager(config = pagingConfig) {
                database.asteroidDao.getAsteroidsList(startDate, endDate)
            }.flow

            Result.success(result)
        }
    }

    suspend fun getImageOfToday(): Result<Flow<ImageOfTodayModel?>> {
        return withContext(ioDispatcher) {
            if (!isNetworkConnected()) {
                return@withContext getImageOfTodayFromDataBase()
            }

            try {
                val flow = getImageOfTheDayFlow()
                val imageData = flow.first()
                if (imageData != null) {
                    database.imageOfTodayDao.insertImageOfToday(imageData)
                }
                Result.success(flow)
            } catch (e: Exception) {
                ensureActive()
                Timber.d("getImageOfToday:Exception: $e")
                getImageOfTodayFromDataBase()
            }
        }
    }


    private suspend fun getImageOfTheDayFlow(): Flow<ImageOfTodayModel?> {
        return withContext(ioDispatcher) {
            flow {
                try {
                    val response = AsteroidApi.retrofitService.getImageOfTheDay()
                    response.creationDate = getTodayDate()
                    Timber.d("getImageOfTheDayFlow:response: $response")
                    emit(response)
                } catch (e: Exception) {
                    Timber.d("getImageOfTheDayFlow:Exception: $e")
                    emit(null)
                }
            }
        }
    }

    private suspend fun getImageOfTodayFromDataBase(): Result<Flow<ImageOfTodayModel?>> {
        return withContext(ioDispatcher) {
            val result = database.imageOfTodayDao.getImageOfToday(getTodayDate())
            Result.success(result)
        }
    }

    suspend fun getAsteroidById(asteroidId: Long): AsteroidModel? {
        return withContext(ioDispatcher) {
            database.asteroidDao.getAsteroidById(asteroidId)
        }
    }

    companion object {
        fun getDummyModel(): AsteroidModel {
            return AsteroidModel(
                codename = "Asteroid Radar",
                closeApproachDate = "2024-04-27",
                isPotentiallyHazardous = false,
                absoluteMagnitude = 11.2,
                estimatedDiameter = 0.2,
                relativeVelocity = 0.1,
                distanceFromEarth = 0.3,
                id = 1
            )
        }

        val fakeAsteroidsList = listOf(
            getDummyModel(),
            getDummyModel(),
            getDummyModel()
        )
    }

}