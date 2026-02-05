package com.udacity.asteroidradar.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.features.main.viewModel.AsteroidUiState
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
    navigateToItemDetail: (AsteroidModel) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Extract state values based on current UI state
    val (isLoading, asteroidPagingItems, imageOfToday) = when (uiState) {
        is AsteroidUiState.Success -> Triple(
            false,
            (uiState as AsteroidUiState.Success).asteroidModelModelList?.collectAsLazyPagingItems(),
            (uiState as AsteroidUiState.Success).imageOfToday
        )
        is AsteroidUiState.Loading -> Triple(
            true,
            (uiState as AsteroidUiState.Loading).asteroidModelModelList?.collectAsLazyPagingItems(),
            (uiState as AsteroidUiState.Loading).imageOfToday
        )
        is AsteroidUiState.Error -> Triple(
            false,
            null,
            null
        )
    }
    
    val isError = uiState is AsteroidUiState.Error
    
    HomeScreen(
        modifier = modifier,
        isLoading = isLoading,
        isError = isError,
        asteroidPagingItems = asteroidPagingItems,
        imageOfToday = imageOfToday,
        onFilterClick = viewModel::updateFilter,
        onItemClick = navigateToItemDetail
    )
}
