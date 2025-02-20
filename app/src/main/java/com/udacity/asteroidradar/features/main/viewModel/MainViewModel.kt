package com.udacity.asteroidradar.features.main.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.BaseViewModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

sealed interface AsteroidUiState {
    data class Success(
        val asteroidModelList: Flow<PagingData<AsteroidModel>>? = null,
        val imageOfToday: ImageOfTodayModel? = null
    ) : AsteroidUiState

    object Error : AsteroidUiState
    data class Loading(
        val asteroidModelList: Flow<PagingData<AsteroidModel>>? = null,
        val imageOfToday: ImageOfTodayModel? = null
    ) : AsteroidUiState
}

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val asteroidRepository: AsteroidRepository,
    application: Application
) : BaseViewModel(application) {

    var asteroidUiState: AsteroidUiState by mutableStateOf(AsteroidUiState.Loading())
        private set

    init {
        refreshList(AsteroidApiFilter.SHOW_TODAY)
        getImageOfToday()
    }


    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidUiState = when (val previousState = asteroidUiState) {
                is AsteroidUiState.Success -> AsteroidUiState.Loading(
                    asteroidModelList = previousState.asteroidModelList,
                    imageOfToday = previousState.imageOfToday
                )

                is AsteroidUiState.Loading -> previousState
                else -> AsteroidUiState.Loading()
            }

            val asteroidPagingFlow = asteroidRepository.refreshAsteroids(filter)
                .getOrNull()?.cachedIn(viewModelScope) ?: flowOf(PagingData.empty())

            asteroidUiState = AsteroidUiState.Success(
                asteroidModelList = asteroidPagingFlow,
                imageOfToday = (asteroidUiState as? AsteroidUiState.Loading)?.imageOfToday
            )
        }
    }

    private fun getImageOfToday() {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidRepository.getImageOfToday().getOrNull()?.collect { imageOfToday ->
                asteroidUiState =
                    (asteroidUiState as? AsteroidUiState.Success)?.copy(imageOfToday = imageOfToday)
                        ?: (asteroidUiState as? AsteroidUiState.Loading)?.copy(imageOfToday = imageOfToday)
                                ?: AsteroidUiState.Success(imageOfToday = imageOfToday)
            }
        }
    }
}