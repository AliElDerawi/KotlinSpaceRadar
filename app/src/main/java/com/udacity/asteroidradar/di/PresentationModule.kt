package com.udacity.asteroidradar.di

import com.udacity.asteroidradar.features.detail.viewModel.DetailScreenViewModel
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for Presentation layer
 * Contains ViewModels
 */
val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::DetailScreenViewModel)
}

