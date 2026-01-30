package com.udacity.asteroidradar.di

import com.udacity.asteroidradar.data.database.AsteroidDao
import com.udacity.asteroidradar.data.database.AsteroidDatabase
import com.udacity.asteroidradar.data.database.ImageOfTodayDao
import com.udacity.asteroidradar.data.database.getDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for Database
 * Contains Room database and DAOs
 */
val databaseModule = module {
    // Database
    single<AsteroidDatabase> {
        getDatabase(androidContext())
    }

    // DAOs
    single<AsteroidDao> {
        get<AsteroidDatabase>().asteroidDao
    }

    single<ImageOfTodayDao> {
        get<AsteroidDatabase>().imageOfTodayDao
    }
}

