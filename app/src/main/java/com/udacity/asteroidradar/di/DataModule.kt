package com.udacity.asteroidradar.di

import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.data.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.data.source.local.AsteroidLocalDataSourceImpl
import com.udacity.asteroidradar.data.source.remote.AsteroidRemoteDataSource
import com.udacity.asteroidradar.data.source.remote.AsteroidRemoteDataSourceImpl
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * Koin module for Data layer
 * Contains Repository implementations and Data Sources
 */
val dataModule = module {
    // Repository
    single<AsteroidRepository> {
        AsteroidRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            ioDispatcher = Dispatchers.IO
        )
    }

    // Data Sources
    single<AsteroidRemoteDataSource> {
        AsteroidRemoteDataSourceImpl()
    }

    single<AsteroidLocalDataSource> {
        AsteroidLocalDataSourceImpl(
            asteroidDao = get(),
            imageOfDayDao = get()
        )
    }
}

