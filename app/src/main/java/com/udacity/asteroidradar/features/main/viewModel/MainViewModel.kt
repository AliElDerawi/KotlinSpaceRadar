package com.udacity.asteroidradar.features.main.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    BaseViewModel(application) {

    private var _asteroidListStateFlow =
        MutableStateFlow<PagingData<AsteroidModel>>(PagingData.empty())
    val asteroidListStateFlow: StateFlow<PagingData<AsteroidModel>>
        get() = _asteroidListStateFlow

    private var _currentSelectedItemLiveData = MutableLiveData<Int>(0)
    val currentSelectedItemLiveData: MutableLiveData<Int>
        get() = _currentSelectedItemLiveData

    val statusStateFlow = asteroidRepository.statusMutableStateFlow

    private var _imageOfTheDayStateFlow = MutableStateFlow<ImageOfTodayModel?>(null)
    val imageOfTheDayStateFlow: StateFlow<ImageOfTodayModel?>
        get() = _imageOfTheDayStateFlow

    init {
        refreshList(AsteroidApiFilter.SHOW_TODAY)
        getImageOfToday()
    }

    fun updateSelectedItem(currentPosition: Int) {
        Timber.d("updateSelectedItem: $currentPosition")
        _currentSelectedItemLiveData.value = currentPosition
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidRepository.refreshAsteroids(filter).getOrNull()?.cachedIn(viewModelScope)
                ?.collectLatest { list ->
                    _asteroidListStateFlow.value = list
                }
        }
    }

    private fun getImageOfToday() {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidRepository.getImageOfToday().getOrNull()?.collect { imageOfToday ->
                _imageOfTheDayStateFlow.value = imageOfToday
            }
        }
    }

}