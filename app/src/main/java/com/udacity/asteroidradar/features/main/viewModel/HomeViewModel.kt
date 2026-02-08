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

data class HomeUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val asteroidPagingDataFlow: Flow<PagingData<AsteroidModel>>? = null,
    val imageOfDayModel: ImageOfDayModel? = null
)

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAsteroidsUseCase: GetAsteroidsUseCase,
    private val getImageOfDayUseCase: GetImageOfDayUseCase,
    private val asteroidRepository: AsteroidRepository,
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        refreshList(AsteroidApiFilter.SHOW_TODAY)
        getImageOfToday()
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        refreshList(filter)
    }

    private fun refreshList(filter: AsteroidApiFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.update { it.copy(isLoading = true, isError = false) }

            // Refresh data from remote source
            asteroidRepository.refreshAsteroids(filter)

            // Get asteroids using use case
            val asteroidPagingFlow = getAsteroidsUseCase(filter)
                .cachedIn(viewModelScope)

            _homeUiState.update {
                it.copy(
                    isLoading = false,
                    asteroidPagingDataFlow = asteroidPagingFlow
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
                    _homeUiState.update { it.copy(imageOfDayModel = imageOfToday) }
                }
            } catch (e: Exception) {
                // If there's an error fetching the image, keep the current state with null image
                // This prevents crashes and shows the placeholder instead
                _homeUiState.update { it.copy(imageOfDayModel = null) }
            }
        }
    }
}