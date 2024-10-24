package com.udacity.asteroidradar.features.main.viewModel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    BaseViewModel(application) {

    private val _startDateMutableStateFlow = MutableStateFlow<String>(getTodayDate())

    private val _endDateMutableStateFlow = MutableStateFlow<String>(getEndDate())

    var asteroidListMutableStateFlow =
        MutableStateFlow<PagingData<AsteroidModel>>(PagingData.empty())

    val statusLiveData = asteroidRepository.statusLiveData

    var imageOfTheDayMutableStateFlow = MutableStateFlow<ImageOfTodayModel?>(null)

    init {

        refreshList(AsteroidApiFilter.SHOW_TODAY)

        getImageOfToday()

        _startDateMutableStateFlow.value = getTodayDate()
        _endDateMutableStateFlow.value = getEndDate()
    }


    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = asteroidRepository.refreshAsteroids(filter)
            result.let {
                if (it.isSuccess) {
                    it.getOrNull()!!.cachedIn(viewModelScope).collectLatest { list ->
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