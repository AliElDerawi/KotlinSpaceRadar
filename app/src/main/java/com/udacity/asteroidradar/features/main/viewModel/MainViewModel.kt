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

    var asteroidListMutableStateFlow =
        MutableStateFlow<PagingData<AsteroidModel>>(PagingData.empty())

    val statusLiveData = asteroidRepository.statusLiveData

    var imageOfTheDayMutableStateFlow = MutableStateFlow<ImageOfTodayModel?>(null)

    init {

        refreshList(AsteroidApiFilter.SHOW_TODAY)

        getImageOfToday()

    }


    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

   private fun refreshList(filter: AsteroidApiFilter) {
    viewModelScope.launch(Dispatchers.IO) {
        asteroidRepository.refreshAsteroids(filter).getOrNull()?.cachedIn(viewModelScope)?.collectLatest { list ->
            asteroidListMutableStateFlow.value = list
        }
    }
}

   private fun getImageOfToday() {
    viewModelScope.launch(Dispatchers.IO) {
        asteroidRepository.getImageOfToday().getOrNull()?.collect { imageOfToday ->
            imageOfTheDayMutableStateFlow.value = imageOfToday
        }
    }
}


}