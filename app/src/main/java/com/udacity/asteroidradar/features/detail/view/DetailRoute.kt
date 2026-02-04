package com.udacity.asteroidradar.features.detail.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.udacity.asteroidradar.features.detail.viewModel.DetailScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AsteroidDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val asteroidModel = viewModel.asteroidModel
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    
    AsteroidDetailScreen(
        modifier = modifier,
        asteroidModel = asteroidModel,
        isLoading = isLoading,
        isError = isError,
        onNavigateBack = navigateBack
    )
}
