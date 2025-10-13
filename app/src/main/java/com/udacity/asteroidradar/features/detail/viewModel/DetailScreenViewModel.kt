package com.udacity.asteroidradar.features.detail.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.features.detail.view.AsteroidDetailDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val asteroidRepository: AsteroidRepository,
    application: Application
) : AndroidViewModel(application) {

    private val asteroidDetailDestination = savedStateHandle.toRoute<AsteroidDetailDestination>()
    private val asteroidId: Long = asteroidDetailDestination.asteroidId

    var asteroidModel: AsteroidModel? by mutableStateOf(null)
        private set

    init {
        loadAsteroid()
    }

    private fun loadAsteroid() {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidModel = asteroidRepository.getAsteroidById(asteroidId)
        }
    }
}

