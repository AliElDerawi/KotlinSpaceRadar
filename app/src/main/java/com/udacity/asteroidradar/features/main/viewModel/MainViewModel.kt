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
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import com.udacity.asteroidradar.domain.usecase.GetAsteroidsUseCase
import com.udacity.asteroidradar.domain.usecase.GetImageOfDayUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

sealed interface AsteroidUiState {
    data class Success(
        val asteroidModelModelList: Flow<PagingData<AsteroidModel>>? = null,
        val imageOfToday: ImageOfDayModel? = null
    ) : AsteroidUiState

    object Error : AsteroidUiState
    data class Loading(
        val asteroidModelModelList: Flow<PagingData<AsteroidModel>>? = null,
        val imageOfToday: ImageOfDayModel? = null
    ) : AsteroidUiState
}

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAsteroidsUseCase: GetAsteroidsUseCase,
    private val getImageOfDayUseCase: GetImageOfDayUseCase,
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
                    asteroidModelModelList = previousState.asteroidModelModelList,
                    imageOfToday = previousState.imageOfToday
                )

                is AsteroidUiState.Loading -> previousState
                else -> AsteroidUiState.Loading()
            }

            // Refresh data from remote source
            asteroidRepository.refreshAsteroids(filter)

            // Get asteroids using use case
            val asteroidPagingFlow = getAsteroidsUseCase(filter)
                .cachedIn(viewModelScope)

            asteroidUiState = AsteroidUiState.Success(
                asteroidModelModelList = asteroidPagingFlow,
                imageOfToday = (asteroidUiState as? AsteroidUiState.Loading)?.imageOfToday
            )
        }
    }

    private fun getImageOfToday() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Refresh image from remote source
                asteroidRepository.refreshImageOfDay()

                // Get image using use case
                getImageOfDayUseCase().collect { imageOfToday ->
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