package com.udacity.asteroidradar.data.repository

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    val statusLiveData = MutableStateFlow<AsteroidApiStatus>(AsteroidApiStatus.DONE)

    suspend fun refreshAsteroids(
        filter: AsteroidApiFilter
    ): Result<Flow<List<AsteroidModel>>> {

        statusLiveData.value = AsteroidApiStatus.LOADING

        if (isNetworkConnected()) {
            if (filter.value == AsteroidApiFilter.SHOW_SAVED.value) {
                val result = database.asteroidDao.getAsteroidsList(
                    getTodayDate(), getEndDate()
                )
                statusLiveData.value = AsteroidApiStatus.DONE

                return Result.success(
                    result
                )

            } else {

                try {

                    val startDate = getTodayDate()
                    val endDate = when (filter) {
                        AsteroidApiFilter.SHOW_WEEK -> getEndDate()
                        AsteroidApiFilter.SHOW_TODAY -> getTodayDate()
                        else -> getTodayDate()
                    }

                    val response = AsteroidApi.retrofitService.getAsteroid(
                        startDate, endDate

                    )
                    Timber.d("getAsteroidList:response: $response")
                    val jsonObject = JSONObject(response)
                    val parseAsteroidsJsonResult = parseAsteroidsJsonResult(jsonObject)
                    val flow = flowOf(parseAsteroidsJsonResult)
                    statusLiveData.value = AsteroidApiStatus.DONE

                    withContext(Dispatchers.IO) {
                        val int =
                            database.asteroidDao.insertAll(*parseAsteroidsJsonResult.toTypedArray())
                        Timber.d("getAsteroidList:insertData: $int")
                    }

                    return Result.success(
                        flow
                    )


                } catch (e: Exception) {
                    statusLiveData.value = AsteroidApiStatus.ERROR
                    Timber.d("getAsteroidList:Exception: $e")
                    return getAsteroidListFromDataBase(filter)

                }

            }
        } else {
            return getAsteroidListFromDataBase(filter)
        }
    }

    private suspend fun getAsteroidListFromDataBase(filter: AsteroidApiFilter): Result<Flow<List<AsteroidModel>>> {

        when (filter) {
            AsteroidApiFilter.SHOW_WEEK -> {

                val result = database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                statusLiveData.value = AsteroidApiStatus.DONE
                return Result.success(result)

            }

            AsteroidApiFilter.SHOW_TODAY -> {

                val result = database.asteroidDao.getAsteroidsList(getTodayDate(), getTodayDate())
                statusLiveData.value = AsteroidApiStatus.DONE
                return Result.success(result)

            }

            else -> {
                val result = database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                statusLiveData.value = AsteroidApiStatus.DONE
                return Result.success(result)

            }
        }

    }

    suspend fun getImageOfToday(): Result<Flow<ImageOfTodayModel>> {

        if (isNetworkConnected()) {
            try {
                val flow = getImageOfTheDayFlow()
                withContext(Dispatchers.IO) {
                    database.imageOfTodayDao.insertImageOfToday(flow.first())
                }
                return Result.success(flow)
            } catch (e: Exception) {
                Timber.d("getImageOfToday:Exception: $e")
                return getImageOfTodayFromDataBase()
            }
        } else {
            return getImageOfTodayFromDataBase()
        }
    }

    private fun getImageOfTheDayFlow(): Flow<ImageOfTodayModel> = flow {
        val response =
            AsteroidApi.retrofitService.getImageOfTheDay()
        emit(response)
    }

    private suspend fun getImageOfTodayFromDataBase(): Result<Flow<ImageOfTodayModel>> {

        val result = database.imageOfTodayDao.getImageOfToday(getTodayDate())
        return Result.success(result)

    }

}