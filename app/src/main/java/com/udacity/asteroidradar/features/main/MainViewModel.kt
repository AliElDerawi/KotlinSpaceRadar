package com.udacity.asteroidradar.features.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    BaseViewModel(application) {

    private val _startDate = MutableStateFlow<String>(getTodayDate())

    private val _endDate = MutableStateFlow<String>(getEndDate())


    var asteroidListMutableStateFlow = MutableStateFlow<List<AsteroidModel>>(arrayListOf())
    val statusLiveData = asteroidRepository.statusLiveData
    var imageOfTheDayMutableStateFlow = MutableStateFlow<ImageOfTodayModel?>(null)

    init {
        viewModelScope.launch {
            refreshList(AsteroidApiFilter.SHOW_WEEK)
        }
        viewModelScope.launch {
            getImageOfToday()
        }
        _startDate.value = getTodayDate()
        _endDate.value = getEndDate()
    }


    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch {
            val result = asteroidRepository.refreshAsteroids(filter)
            result.let {
                if (it.isSuccess) {
                    it.getOrNull()!!.collect { list ->
                        asteroidListMutableStateFlow.value = list
                    }
                } else {

                }
            }
        }
    }

    private fun getImageOfToday() {
        viewModelScope.launch {
            val result = asteroidRepository.getImageOfToday()
            result.let {
                if (it.isSuccess) {
                    it.getOrNull()!!.collect { imageOfToday ->
                        imageOfTheDayMutableStateFlow.value = imageOfToday
                    }
                } else {

                }
            }
        }
    }


}