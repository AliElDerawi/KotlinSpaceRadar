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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    val statusLiveData = MutableStateFlow<AsteroidApiStatus>(AsteroidApiStatus.DONE)

    val pagingConfig = PagingConfig(pageSize = 10, prefetchDistance = 5, enablePlaceholders = false)



    suspend fun refreshAsteroids(
        filter: AsteroidApiFilter
    ): Result<Flow<PagingData<AsteroidModel>>> {

        statusLiveData.value = AsteroidApiStatus.LOADING

        if (isNetworkConnected()) {
            if (filter.value == AsteroidApiFilter.SHOW_SAVED.value) {

                val result = Pager(config = pagingConfig,
                    pagingSourceFactory = {
                        database.asteroidDao.getAsteroidsList(
                            getTodayDate(), getEndDate()
                        )
                    }).flow

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
                    Timber.d("getAsteroidList:parseAsteroidsJsonResult: $parseAsteroidsJsonResult")
                    statusLiveData.value = AsteroidApiStatus.DONE

                    withContext(Dispatchers.IO) {
                        val int =
                            database.asteroidDao.insertAll(*parseAsteroidsJsonResult.toTypedArray())
                        Timber.d("getAsteroidList:insertData: $int")
                    }

                    return Result.success(
                        Pager(config = pagingConfig,
                            pagingSourceFactory = { ApiPagingSource(parseAsteroidsJsonResult) }).flow
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

    private suspend fun getAsteroidListFromDataBase(filter: AsteroidApiFilter): Result<Flow<PagingData<AsteroidModel>>> {

        when (filter) {
            AsteroidApiFilter.SHOW_WEEK -> {

                val result = Pager(config = pagingConfig,
                    pagingSourceFactory = {
                        database.asteroidDao.getAsteroidsList(
                            getTodayDate(), getEndDate()
                        )
                    }).flow

                statusLiveData.value = AsteroidApiStatus.DONE
                return Result.success(result)

            }

            AsteroidApiFilter.SHOW_TODAY -> {

                val result = Pager(config = pagingConfig,
                    pagingSourceFactory = {
                        database.asteroidDao.getAsteroidsList(
                            getTodayDate(), getTodayDate()
                        )
                    }).flow

                statusLiveData.value = AsteroidApiStatus.DONE
                return Result.success(result)

            }

            else -> {
                val result = Pager(config = pagingConfig,
                    pagingSourceFactory = {
                        database.asteroidDao.getAsteroidsList(
                            getTodayDate(), getEndDate()
                        )
                    }).flow
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
        val response = AsteroidApi.retrofitService.getImageOfTheDay()
        Timber.d("getImageOfTheDayFlow:response: $response")
        emit(response)
    }

    private suspend fun getImageOfTodayFromDataBase(): Result<Flow<ImageOfTodayModel>> {

        val result = database.imageOfTodayDao.getImageOfToday(getTodayDate())
        return Result.success(result)

    }

}