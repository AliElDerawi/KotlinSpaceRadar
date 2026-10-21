package com.udacity.asteroidradar.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val imageOfDayModel: ImageOfDayModel? = null
)

class HomeViewModel(
    private val asteroidRepository: AsteroidRepository,
) : ViewModel() {

    private val currentFilter = MutableStateFlow(AsteroidApiFilter.SHOW_TODAY)
    private val isRefreshing = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val asteroidPagingDataFlow: Flow<PagingData<AsteroidModel>> = currentFilter
        .flatMapLatest { filter -> asteroidRepository.getAsteroids(filter) }
        .cachedIn(viewModelScope)

    val homeUiState: StateFlow<HomeUiState> = combine(
        isRefreshing,
        asteroidRepository.getImageOfDay().catch { emit(null) }
    ) { loading, imageOfDay ->
        HomeUiState(
            isLoading = loading,
            imageOfDayModel = imageOfDay
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        refreshData(currentFilter.value)
        viewModelScope.launch { asteroidRepository.refreshImageOfDay() }
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        currentFilter.value = filter
        refreshData(filter)
    }

    private fun refreshData(filter: AsteroidApiFilter) {
        viewModelScope.launch {
            isRefreshing.value = true
            asteroidRepository.refreshAsteroids(filter)
            isRefreshing.value = false
        }
    }
}