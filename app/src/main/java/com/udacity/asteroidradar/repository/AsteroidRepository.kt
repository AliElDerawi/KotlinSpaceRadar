package com.udacity.asteroidradar.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.AsteroidApiStatus
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.isNetworkConnected
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseImageOfTodayJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase,
    private val mLifecycleOwner: LifecycleOwner?
) {


    var asteroidsLiveData = MutableLiveData<List<AsteroidModel>>()

    val statusLiveData = MutableLiveData<AsteroidApiStatus>()

    var imageOfTodayLiveData = MutableLiveData<ImageOfTodayModel>()


    suspend fun refreshAsteroids(
        filter: AsteroidApiFilter
    ) {


        statusLiveData.value = AsteroidApiStatus.LOADING

        if (isNetworkConnected()) {
            if (filter.value == AsteroidApiFilter.SHOW_SAVED.value) {

                database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                    .observe(mLifecycleOwner!!) {
                        if (it != null) {
                            Timber.d(
                                "getAsteroidList:asteroidModelList:size ${it.size}"
                            )

                            asteroidsLiveData.value = it

                        } else {
                            Timber.d("getAsteroidList:asteroidModelList:noData")
                        }

                        statusLiveData.value = AsteroidApiStatus.DONE

                    }


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
                    asteroidsLiveData.value = parseAsteroidsJsonResult
                    statusLiveData.value = AsteroidApiStatus.DONE


                    withContext(Dispatchers.IO) {
                        val int =
                            database.asteroidDao.insertAll(*parseAsteroidsJsonResult.toTypedArray())
                        Timber.d("getAsteroidList:insertData: $int")
                    }


                } catch (e: Exception) {
                    statusLiveData.value = AsteroidApiStatus.ERROR
//                    _asteroidListLiveData.value = ArrayList()
                    getAsteroidListFromDataBase(filter)
                    Timber.d("getAsteroidList:Exception: $e")
                }

            }
        } else {
            getAsteroidListFromDataBase(filter)
        }
    }

    private suspend fun getAsteroidListFromDataBase(filter: AsteroidApiFilter) {
        when (filter) {
            AsteroidApiFilter.SHOW_WEEK -> {

                database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                    .observe(mLifecycleOwner!!) {
                        if (it != null) {
                            Timber.d(
                                "getAsteroidList:asteroidModelList:size ${it.size}"
                            )

                            asteroidsLiveData.value = it

                        } else {
                            Timber.d("getAsteroidList:asteroidModelList:noData")
                        }

                        statusLiveData.value = AsteroidApiStatus.DONE

                    }

            }

            AsteroidApiFilter.SHOW_TODAY -> {

                database.asteroidDao.getAsteroidsList(getTodayDate(), getTodayDate())
                    .observe(mLifecycleOwner!!) {
                        if (it != null) {
                            Timber.d(
                                "getAsteroidList:asteroidModelList:size ${it.size}"
                            )

                            asteroidsLiveData.value = it

                        } else {
                            Timber.d("getAsteroidList:asteroidModelList:noData")
                        }

                        statusLiveData.value = AsteroidApiStatus.DONE

                    }

            }

            else -> {
                database.asteroidDao.getAsteroidsList(getTodayDate(), getEndDate())
                    .observe(mLifecycleOwner!!) {
                        if (it != null) {
                            Timber.d(
                                "getAsteroidList:asteroidModelList:size ${it.size}"
                            )

                            asteroidsLiveData.value = it

                        } else {
                            Timber.d("getAsteroidList:asteroidModelList:noData")
                        }

                        statusLiveData.value = AsteroidApiStatus.DONE

                    }
            }
        }

    }

    suspend fun getImageOfToday() {


        if (isNetworkConnected()) {
            try {
                val response = AsteroidApi.retrofitService.getImageOfTheDay()
                val imageOfToday = parseImageOfTodayJsonResult(JSONObject(response))
                imageOfTodayLiveData.value = imageOfToday
                withContext(Dispatchers.IO) {
                    database.imageOfTodayDao.insertImageOfToday(imageOfToday)
                }
            } catch (e: Exception) {
                Timber.d("getImageOfToday:Exception: $e")
                getImageOfTodayFromDataBase()
            }
        } else {
            getImageOfTodayFromDataBase()
        }
    }

    private suspend fun getImageOfTodayFromDataBase() {
        database.imageOfTodayDao.getImageOfToday(getTodayDate()).observe(mLifecycleOwner!!) {
            if (it != null) {
                imageOfTodayLiveData.value = it
            }
        }
    }

}