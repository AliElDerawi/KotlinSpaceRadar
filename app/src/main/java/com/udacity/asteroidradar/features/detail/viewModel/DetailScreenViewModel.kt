package com.udacity.asteroidradar.features.detail.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import com.udacity.asteroidradar.navigation.AsteroidDetailDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class DetailUiState(
    val asteroidModel: AsteroidModel? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false
)

class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val asteroidRepository: AsteroidRepository,
) : ViewModel() {

    private val asteroidDetailDestination = savedStateHandle.toRoute<AsteroidDetailDestination>()
    private val asteroidId: Long = asteroidDetailDestination.asteroidId
    private val _detailUiState = MutableStateFlow(DetailUiState())
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    init {
        loadAsteroid()
    }

    private fun loadAsteroid() {
        viewModelScope.launch() {
            _detailUiState.update { it.copy(isLoading = true, isError = false) }
            
            val result = asteroidRepository.getAsteroidById(asteroidId)
            
            result.fold(
                onSuccess = { asteroid ->
                    _detailUiState.update {
                        it.copy(
                            asteroidModel = asteroid,
                            isLoading = false,
                            isError = false
                        )
                    }
                },
                onFailure = {
                    _detailUiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            )
        }
    }
}

