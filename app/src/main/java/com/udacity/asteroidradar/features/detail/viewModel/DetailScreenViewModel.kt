package com.udacity.asteroidradar.features.detail.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.usecase.GetAsteroidByIdUseCase
import com.udacity.asteroidradar.navigation.AsteroidDetailDestination
import kotlinx.coroutines.Dispatchers
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
    private val getAsteroidByIdUseCase: GetAsteroidByIdUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val asteroidDetailDestination = savedStateHandle.toRoute<AsteroidDetailDestination>()
    private val asteroidId: Long = asteroidDetailDestination.asteroidId

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadAsteroid()
    }

    private fun loadAsteroid() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            
            val result = getAsteroidByIdUseCase(asteroidId)
            
            result.fold(
                onSuccess = { asteroid ->
                    _uiState.update { 
                        it.copy(
                            asteroidModel = asteroid,
                            isLoading = false,
                            isError = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { 
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

