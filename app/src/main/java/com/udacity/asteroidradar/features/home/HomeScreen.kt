package com.udacity.asteroidradar.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.ImageOfDayModel
import com.udacity.asteroidradar.features.main.view.AsteroidAppTopBar
import com.udacity.asteroidradar.features.main.viewModel.AsteroidUiState
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.theme.md_theme_light_scrim
import com.udacity.asteroidradar.util.dimenToSp
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
object HomeDestination {
    val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    navigateToItemDetail: (asteroidModel: AsteroidModel) -> Unit,
) {
    val asteroidUiState = viewModel.asteroidUiState
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AsteroidAppTopBar(
                title = stringResource(HomeDestination.titleRes),
                scrollBehavior = scrollBehavior,
                canNavigateBack = false,
                onFilterClick = { filter ->
                    viewModel.updateFilter(filter)
                },
            )
        },
    ) { innerPadding ->
        when (asteroidUiState) {

            is AsteroidUiState.Loading -> {
                LoadingScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                )
            }

            is AsteroidUiState.Success -> {
                val asteroidPagingItems =
                    asteroidUiState.asteroidModelModelList?.collectAsLazyPagingItems()
                val imageOfTodayModel = asteroidUiState.imageOfToday
                HomeBody(
                    itemList = asteroidPagingItems,
                    imageOfTodayModel = imageOfTodayModel,
                    onItemClick = navigateToItemDetail,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = innerPadding
                )
            }

            is AsteroidUiState.Error -> {

            }
        }
    }
}

@Composable
private fun HomeBody(
    itemList: LazyPagingItems<AsteroidModel>? = null,
    imageOfTodayModel: ImageOfDayModel? = null,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemClick: (AsteroidModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), contentPadding = contentPadding
    ) {
        // Header item (Image of Today or Placeholder)
        item {
            if (imageOfTodayModel != null && imageOfTodayModel.url.isNotEmpty()) {
                HomeHeader(imageOfTodayModel = imageOfTodayModel, modifier = modifier)
            } else {
                ImageOfTodayPlaceholder(modifier = modifier)
            }
        }

        // Show no data message if the list is empty and loading state is active
        itemList?.let { list ->
            if (list.loadState.refresh !is LoadState.Loading && list.itemCount == 0) {
                item {
                    HomeNoDataMessage()
                }
            } else {
                // List items
                items(list.itemCount) { index ->
                    list[index]?.let { asteroid ->
                        AsteroidItem(
                            asteroidModel = asteroid,
                            modifier = Modifier.clickable { onItemClick(asteroid) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageOfToday(imageOfTodayModel: ImageOfDayModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(color = md_theme_light_scrim)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageOfTodayModel.url)
                .crossfade(true).build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = imageOfTodayModel.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = md_theme_light_scrim)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(color = md_theme_light_scrim),
        ) {
            Text(
                text = imageOfTodayModel.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                color = Color.White,
                lineHeight = 26.sp,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.dim_default_margin),
                        end = dimensionResource(R.dimen.dim_default_margin),
                        top = dimensionResource(R.dimen.dim_default_margin),
                        bottom = dimensionResource(R.dimen.dim_default_margin)
                    )
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ImageOfTodayPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(color = md_theme_light_scrim),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(dimensionResource(R.dimen.dim_default_margin))
        ) {
            // Placeholder icon/shape
            Icon(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = stringResource(R.string.text_empty_picture_of_today),
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder text
            Text(
                text = stringResource(R.string.text_empty_picture_of_today),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = md_theme_light_scrim)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(R.drawable.loading_img).crossfade(true).build(),
            contentDescription = stringResource(R.string.text_description_loading_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = md_theme_light_scrim)
        )
    }
}

@Composable
private fun AsteroidItem(
    asteroidModel: AsteroidModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dim_default_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name and date
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = dimensionResource(R.dimen.dim_small_margin))
        ) {
            Text(
                text = asteroidModel.codename,
                fontSize = dimenToSp(R.dimen.text_normal),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_4dp)))
            Text(
                text = asteroidModel.closeApproachDate,
                fontSize = dimenToSp(R.dimen.text_small),
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Hazardous status icon and description
        var hazardousDescription: String = ""
        var iconRes: Int = 0

        if (asteroidModel.isPotentiallyHazardous) {
            iconRes = R.drawable.ic_status_potentially_hazardous // Replace with your hazardous icon
            hazardousDescription =
                stringResource(R.string.text_description_potentially_hazardous_asteroid_image)
        } else {
            iconRes = R.drawable.ic_status_normal // Replace with your normal icon
            hazardousDescription =
                stringResource(R.string.text_description_not_hazardous_asteroid_image)
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = hazardousDescription,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// Preview helper functions
private fun getDummyAsteroid(): AsteroidModel {
    return AsteroidModel(
        codename = "Asteroid Radar",
        closeApproachDate = "2024-04-27",
        isPotentiallyHazardous = false,
        absoluteMagnitude = 11.2,
        estimatedDiameter = 0.2,
        relativeVelocity = 0.1,
        distanceFromEarth = 0.3,
        id = 1
    )
}

private fun getDummyImageOfDay(): ImageOfDayModel {
    return ImageOfDayModel(
        title = "Sample Image",
        url = "",
        mediaType = "image",
        date = "2024-04-27"
    )
}

private val fakeAsteroidsList = listOf(
    getDummyAsteroid(),
    getDummyAsteroid().copy(id = 2, codename = "Asteroid 2"),
    getDummyAsteroid().copy(id = 3, codename = "Asteroid 3")
)

@Preview
@Composable
fun PreviewAsteroidItem() {
    AsteroidItem(
        asteroidModel = getDummyAsteroid(),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    )
}

@Preview(showBackground = true)
@Composable
private fun ImageOfTodayPreview() {
    ImageOfToday(
        imageOfTodayModel = getDummyImageOfDay(), modifier = Modifier.background(Color.Black)
    )
}

@Preview(showBackground = true)
@Composable
private fun ImageOfTodayPlaceholderPreview() {
    ImageOfTodayPlaceholder(
        modifier = Modifier.background(Color.Black)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyPreview() {
    HomeBody(
        imageOfTodayModel = getDummyImageOfDay(),
        itemList = fakeLazyPagingItems(fakeAsteroidsList),
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        onItemClick = {},
    )
}

@Composable
private fun HomeHeader(imageOfTodayModel: ImageOfDayModel, modifier: Modifier = Modifier) {
    ImageOfToday(
        imageOfTodayModel = imageOfTodayModel, modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun HomeNoDataMessage(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.no_data),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dim_default_margin))
    )
}

@Composable
fun <T : Any> fakeLazyPagingItems(data: List<T>): LazyPagingItems<T> {
    val fakeFlow = remember { flowOf(PagingData.from(data)) }
    val pagingData = fakeFlow.collectAsLazyPagingItems()
    return pagingData
}