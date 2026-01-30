package com.udacity.asteroidradar.di

import com.udacity.asteroidradar.domain.usecase.GetAsteroidByIdUseCase
import com.udacity.asteroidradar.domain.usecase.GetAsteroidsUseCase
import com.udacity.asteroidradar.domain.usecase.GetImageOfDayUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Koin module for Domain layer
 * Contains Use Cases
 */
val domainModule = module {
    factoryOf(::GetAsteroidsUseCase)
    factoryOf(::GetAsteroidByIdUseCase)
    factoryOf(::GetImageOfDayUseCase)
}

