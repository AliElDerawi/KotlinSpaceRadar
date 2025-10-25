package com.udacity.asteroidradar.features.main.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
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
) : AndroidViewModel(application) {

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
            try {
                asteroidRepository.getImageOfToday().getOrNull()?.collect { imageOfToday ->
                    asteroidUiState =
                        (asteroidUiState as? AsteroidUiState.Success)?.copy(imageOfToday = imageOfToday)
                            ?: (asteroidUiState as? AsteroidUiState.Loading)?.copy(imageOfToday = imageOfToday)
                                    ?: AsteroidUiState.Success(imageOfToday = imageOfToday)
                }
            } catch (e: Exception) {
                // If there's an error fetching the image, keep the current state with null image
                // This prevents crashes and shows the placeholder instead
                asteroidUiState = when (val currentState = asteroidUiState) {
                    is AsteroidUiState.Success -> currentState.copy(imageOfToday = null)
                    is AsteroidUiState.Loading -> currentState.copy(imageOfToday = null)
                    else -> AsteroidUiState.Success(imageOfToday = null)
                }
            }
        }
    }
}