package com.udacity.asteroidradar.domain.usecase

import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository

/**
 * Use case for getting a specific asteroid by ID
 * Encapsulates the business logic for fetching a single asteroid
 */
class GetAsteroidByIdUseCase(
    private val repository: AsteroidRepository
) {
    /**
     * Invoke operator allows calling the use case like a function
     * @param id The asteroid ID
     * @return Result containing the Asteroid or error
     */
    suspend operator fun invoke(id: Long): Result<AsteroidModel> {
        return repository.getAsteroidById(id)
    }
}

