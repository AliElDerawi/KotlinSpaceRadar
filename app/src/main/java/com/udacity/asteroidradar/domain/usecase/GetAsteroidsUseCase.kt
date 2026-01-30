package com.udacity.asteroidradar.domain.usecase

import androidx.paging.PagingData
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting asteroids with pagination
 * Encapsulates the business logic for fetching asteroids
 */
class GetAsteroidsUseCase(
    private val repository: AsteroidRepository
) {
    /**
     * Invoke operator allows calling the use case like a function
     * @param filter The filter to apply (TODAY, WEEK, SAVED)
     * @return Flow of PagingData containing asteroids
     */
    operator fun invoke(filter: AsteroidApiFilter): Flow<PagingData<AsteroidModel>> {
        return repository.getAsteroids(filter)
    }
}

