/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.asteroidradar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.udacity.asteroidradar.features.detail.view.AsteroidDetailRoute
import com.udacity.asteroidradar.features.home.HomeRoute
import timber.log.Timber

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AsteroidNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination,
        modifier = modifier
    ) {
        composable<HomeDestination> {
            HomeRoute { asteroid ->
                Timber.d("Navigating to AsteroidDetailScreen with asteroid ID: ${asteroid.id}")
                navController.navigate(AsteroidDetailDestination(asteroidId = asteroid.id))
            }
        }

        composable<AsteroidDetailDestination> {
            AsteroidDetailRoute(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
