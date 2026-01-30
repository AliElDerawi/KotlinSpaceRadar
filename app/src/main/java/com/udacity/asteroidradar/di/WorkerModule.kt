package com.udacity.asteroidradar.di

import com.udacity.asteroidradar.work.RefreshDataWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

/**
 * Koin module for WorkManager
 * Contains Workers
 */
val workerModule = module {
    workerOf(::RefreshDataWorker)
}

