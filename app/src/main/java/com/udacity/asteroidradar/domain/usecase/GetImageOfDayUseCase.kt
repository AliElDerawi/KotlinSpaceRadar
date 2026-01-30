package com.udacity.asteroidradar.domain.usecase

import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting the image of the day
 * Encapsulates the business logic for fetching the daily image
 */
class GetImageOfDayUseCase(
    private val repository: AsteroidRepository
) {
    /**
     * Invoke operator allows calling the use case like a function
     * @return Flow of ImageOfDay or null if not available
     */
    operator fun invoke(): Flow<ImageOfDayModel?> {
        return repository.getImageOfDay()
    }
}

