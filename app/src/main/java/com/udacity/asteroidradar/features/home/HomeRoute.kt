package com.udacity.asteroidradar.features.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    val uiState = viewModel.asteroidUiState
    
    // Extract state values based on current UI state
    val (isLoading, asteroidPagingItems, imageOfToday) = when (uiState) {
        is AsteroidUiState.Success -> Triple(
            false,
            uiState.asteroidModelModelList?.collectAsLazyPagingItems(),
            uiState.imageOfToday
        )
        is AsteroidUiState.Loading -> Triple(
            true,
            uiState.asteroidModelModelList?.collectAsLazyPagingItems(),
            uiState.imageOfToday
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
