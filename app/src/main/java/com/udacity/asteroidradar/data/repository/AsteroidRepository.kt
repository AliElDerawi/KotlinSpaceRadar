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
import com.udacity.asteroidradar.util.ApiPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    val statusMutableStateFlow = MutableStateFlow<AsteroidApiStatus>(AsteroidApiStatus.DONE)
    val pagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 0, enablePlaceholders = true)

    suspend fun refreshAsteroids(
        filter: AsteroidApiFilter
    ): Result<Flow<PagingData<AsteroidModel>>> {

        statusMutableStateFlow.value = AsteroidApiStatus.LOADING

        if (!isNetworkConnected()) {
            return getAsteroidListFromDataBase(filter)
        }

        return try {
            if (filter == AsteroidApiFilter.SHOW_SAVED) {
                val result = Pager(pagingConfig) {
                    database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                }.flow
                statusMutableStateFlow.value = AsteroidApiStatus.DONE
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

                withContext(Dispatchers.IO) {
                    database.asteroidDao.insertAll(*asteroids.toTypedArray())
                }

                statusMutableStateFlow.value = AsteroidApiStatus.DONE
                val result = Pager(pagingConfig) { ApiPagingSource(asteroids) }.flow
                Result.success(result)
            }
        } catch (e: Exception) {
            statusMutableStateFlow.value = AsteroidApiStatus.ERROR
            Timber.d("Exception: $e")
            getAsteroidListFromDataBase(filter)
        }
    }


    private suspend fun getAsteroidListFromDataBase(filter: AsteroidApiFilter): Result<Flow<PagingData<AsteroidModel>>> {
        val (startDate, endDate) = when (filter) {
            AsteroidApiFilter.SHOW_TODAY -> getTodayDate() to getTodayDate()
            else -> getTodayDate() to getEndDate()
        }

        val result = Pager(config = pagingConfig) {
            database.asteroidDao.getAsteroidsList(startDate, endDate)
        }.flow

        statusMutableStateFlow.value = AsteroidApiStatus.DONE
        return Result.success(result)
    }

    suspend fun getImageOfToday(): Result<Flow<ImageOfTodayModel>> {
        if (!isNetworkConnected()) {
            return getImageOfTodayFromDataBase()
        }
        return try {
            val flow = getImageOfTheDayFlow()
            withContext(Dispatchers.IO) {
                database.imageOfTodayDao.insertImageOfToday(flow.first())
            }
            Result.success(flow)
        } catch (e: Exception) {
            Timber.d("getImageOfToday:Exception: $e")
            getImageOfTodayFromDataBase()
        }
    }


    private fun getImageOfTheDayFlow(): Flow<ImageOfTodayModel> = flow {
        val response = AsteroidApi.retrofitService.getImageOfTheDay()
        response.creationDate = getTodayDate()
        Timber.d("getImageOfTheDayFlow:response: $response")
        emit(response)
    }

    private suspend fun getImageOfTodayFromDataBase(): Result<Flow<ImageOfTodayModel>> {
        val result = database.imageOfTodayDao.getImageOfToday(getTodayDate())
        return Result.success(result)
    }

}