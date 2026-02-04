package com.udacity.asteroidradar.features.detail.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.udacity.asteroidradar.features.detail.viewModel.DetailScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AsteroidDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    AsteroidDetailScreen(
        modifier = modifier,
        asteroidModel = uiState.asteroidModel,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        onNavigateBack = navigateBack
    )
}
