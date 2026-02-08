package com.udacity.asteroidradar.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
    navigateToItemDetail: (AsteroidModel) -> Unit
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        asteroidPagingItems = uiState.asteroidPagingDataFlow?.collectAsLazyPagingItems(),
        imageOfToday = uiState.imageOfDayModel,
        onFilterClick = viewModel::updateFilter,
        onItemClick = navigateToItemDetail
    )
}
