package com.udacity.asteroidradar.features.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.data.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    BaseViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName


    private val _startDate = MutableLiveData<String>(getTodayDate())

    private val _endDate = MutableLiveData<String>(getEndDate())


//    val imageOfTodayModel: LiveData<ImageOfTodayModel> =
//        database.imageOfTodayDao.getImageOfToday(getTodayDate())
//
//    val imageOfTheDayLiveData = imageOfTodayModel


    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids(AsteroidApiFilter.SHOW_WEEK)
//            asteroidRepository.getImageOfToday()
        }
        viewModelScope.launch {
            asteroidRepository.getImageOfToday()
        }
//        getAsteroidList(AsteroidApiFilter.SHOW_WEEK)
        _startDate.value = getTodayDate()
        _endDate.value = getEndDate()
    }

    val asteroidListLiveData = asteroidRepository.asteroidsLiveData
    val statusLiveData = asteroidRepository.statusLiveData
    val imageOfTheDayLiveData = asteroidRepository.imageOfTodayLiveData

//    private fun getImageOfToday() {
//        viewModelScope.launch {
//            try {
//                val response = AsteroidApi.retrofitService.getImageOfTheDay(Constants.API_KEY)
//                val imageOfToday = parseImageOfTodayJsonResult(JSONObject(response))
//                withContext(Dispatchers.IO) {
//                    database.imageOfTodayDao.insertImageOfToday(imageOfToday)
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "getImageOfToday:Exception: $e")
//            }
//        }
//    }

    fun setAsteroidList() {
        val asteroidList = listOf(
            AsteroidModel(
                1, "Asteroid 1", "2021-03-01", 1.0, 1.0, 1.0, 1.0, false
            ), AsteroidModel(
                2, "Asteroid 2", "2021-03-02", 2.0, 2.0, 2.0, 2.0, true
            ), AsteroidModel(
                3, "Asteroid 3", "2021-03-03", 3.0, 3.0, 3.0, 3.0, false
            )
        )
//        _asteroidListLiveData.value = asteroidList
    }


    fun updateFilter(filter: AsteroidApiFilter) {
        viewModelScope.launch { asteroidRepository.refreshAsteroids(filter) }
    }

//    class Factory(val app: Application, val mLifecycleOwner: LifecycleOwner) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST") return MainViewModel(app, mLifecycleOwner) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


}