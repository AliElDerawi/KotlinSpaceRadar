package com.udacity.asteroidradar.features.detail.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.features.detail.view.AsteroidDetailDestination
import kotlinx.serialization.json.Json


class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val asteroidRepository: AsteroidRepository,
    application: Application
) : AndroidViewModel(application) {

    private val asteroidJson: String? = savedStateHandle[AsteroidDetailDestination.ASTEROID_MODEL_ARG]
    val asteroidModel: AsteroidModel? = asteroidJson?.let { Json.decodeFromString(AsteroidModel.serializer(), it) }

}