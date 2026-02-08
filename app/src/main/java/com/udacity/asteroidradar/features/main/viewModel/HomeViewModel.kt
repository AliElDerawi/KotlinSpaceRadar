package com.udacity.asteroidradar.features.main.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import com.udacity.asteroidradar.domain.usecase.GetAsteroidsUseCase
import com.udacity.asteroidradar.domain.usecase.GetImageOfDayUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
) : ViewModel() {

    private val _uiState = MutableStateFlow<AsteroidUiState>(AsteroidUiState.Loading())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        refreshList(AsteroidApiFilter.SHOW_TODAY)
        getImageOfToday()
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                when (currentState) {
                    is AsteroidUiState.Success -> AsteroidUiState.Loading(
                        asteroidModelModelList = currentState.asteroidModelModelList,
                        imageOfToday = currentState.imageOfToday
                    )
                    is AsteroidUiState.Loading -> currentState
                    else -> AsteroidUiState.Loading()
                }
            }

            // Refresh data from remote source
            asteroidRepository.refreshAsteroids(filter)

            // Get asteroids using use case
            val asteroidPagingFlow = getAsteroidsUseCase(filter)
                .cachedIn(viewModelScope)

            _uiState.update { currentState ->
                AsteroidUiState.Success(
                    asteroidModelModelList = asteroidPagingFlow,
                    imageOfToday = (currentState as? AsteroidUiState.Loading)?.imageOfToday
                )
            }
        }
    }

    private fun getImageOfToday() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Refresh image from remote source
                asteroidRepository.refreshImageOfDay()

                // Get image using use case
                getImageOfDayUseCase().collect { imageOfToday ->
                    _uiState.update { currentState ->
                        when (currentState) {
                            is AsteroidUiState.Success -> currentState.copy(imageOfToday = imageOfToday)
                            is AsteroidUiState.Loading -> currentState.copy(imageOfToday = imageOfToday)
                            else -> AsteroidUiState.Success(imageOfToday = imageOfToday)
                        }
                    }
                }
            } catch (e: Exception) {
                // If there's an error fetching the image, keep the current state with null image
                // This prevents crashes and shows the placeholder instead
                _uiState.update { currentState ->
                    when (currentState) {
                        is AsteroidUiState.Success -> currentState.copy(imageOfToday = null)
                        is AsteroidUiState.Loading -> currentState.copy(imageOfToday = null)
                        else -> AsteroidUiState.Success(imageOfToday = null)
                    }
                }
            }
        }
    }
}