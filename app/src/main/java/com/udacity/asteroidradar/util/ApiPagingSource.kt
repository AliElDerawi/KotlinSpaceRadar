package com.udacity.asteroidradar.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.udacity.asteroidradar.api.models.AsteroidModel

class ApiPagingSource(
    private val apiData: List<AsteroidModel> // The full list of data returned from the API
) : PagingSource<Int, AsteroidModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AsteroidModel> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            // Calculate start and end index for the current page
            val fromIndex = (page - 1) * pageSize
            val toIndex = (fromIndex + pageSize).coerceAtMost(apiData.size)

            // Return a paged subset of data
            val pagedData =
                apiData.subList(fromIndex, toIndex).takeIf { it.isNotEmpty() } ?: emptyList()

            // Check if there's more data
            val nextPage = if (toIndex < apiData.size) page + 1 else null

            LoadResult.Page(
                data = pagedData, prevKey = if (page == 1) null else page - 1, nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AsteroidModel>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }
    }
}
