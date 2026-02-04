package com.udacity.asteroidradar.features.detail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.features.main.view.AsteroidAppTopBar
import com.udacity.asteroidradar.theme.md_theme_light_scrim
import com.udacity.asteroidradar.util.dimenToSp
import kotlinx.serialization.Serializable

@Serializable
data class AsteroidDetailDestination(
    val asteroidId: Long
) {
    companion object {
        val titleRes = R.string.text_asteroid_detail
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailScreen(
    modifier: Modifier = Modifier,
    asteroidModel: AsteroidModel? = null,
    isLoading: Boolean = false,
    isError: Boolean = false,
    onNavigateBack: () -> Unit = {}
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        AsteroidAppTopBar(
            title = asteroidModel?.codename
                ?: stringResource(AsteroidDetailDestination.titleRes),
            canNavigateBack = true,
            showMenu = false,
            navigateUp = onNavigateBack,
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    DetailLoadingScreen(modifier = Modifier.fillMaxSize())
                }
                isError -> {
                    DetailErrorScreen(modifier = Modifier.fillMaxSize())
                }
                asteroidModel != null -> {
                    AsteroidDetail(
                        asteroidModel = asteroidModel,
                        modifier = Modifier.fillMaxSize(),
                        onHelpClick = { showDialog = true },
                    )
                }
                else -> {
                    // Fallback for null asteroid without error state
                    DetailErrorScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    if (showDialog) {
        AstronomicalUnitExplanationDialog(onDismiss = { showDialog = false })
    }
}

@Composable
private fun DetailLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = md_theme_light_scrim),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun DetailErrorScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = md_theme_light_scrim),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(dimensionResource(R.dimen.dim_default_margin))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = stringResource(R.string.text_description_error),
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.text_description_error),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AsteroidDetail(
    asteroidModel: AsteroidModel,
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Image
        Image(
            painter = painterResource(
                if (asteroidModel.isPotentiallyHazardous) R.drawable.asteroid_hazardous
                else R.drawable.asteroid_safe
            ),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_small_margin)))

        // Details
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.dim_default_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dim_default_margin))
        ) {
            DetailItem(
                title = stringResource(R.string.text_close_approach_date),
                value = asteroidModel.closeApproachDate
            )

            DetailItem(
                title = stringResource(R.string.text_absolute_magnitude),
                value = stringResource(
                    R.string.text_format_astronomical_unit, asteroidModel.absoluteMagnitude
                ),
                helpIcon = true,
                onHelpClick = onHelpClick
            )

            DetailItem(
                title = stringResource(R.string.text_estimated_diameter),
                value = stringResource(
                    R.string.text_format_km_unit, asteroidModel.estimatedDiameter
                )
            )

            DetailItem(
                title = stringResource(R.string.text_relative_velocity),
                value = stringResource(
                    R.string.text_format_km_s_unit, asteroidModel.relativeVelocity
                )
            )

            DetailItem(
                title = stringResource(R.string.text_distance_from_earth),
                value = stringResource(
                    R.string.text_format_astronomical_unit, asteroidModel.distanceFromEarth
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_default_margin)))
        }
    }
}

@Composable
fun DetailItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    helpIcon: Boolean = false,
    onHelpClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = dimenToSp(R.dimen.text_normal),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_4dp)))

            Text(
                text = value,
                fontSize = dimenToSp(R.dimen.text_small),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        if (helpIcon && onHelpClick != null) {
            Icon(
                painter = painterResource(R.drawable.ic_help_circle),
                contentDescription = stringResource(R.string.text_description_help_icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(dimensionResource(R.dimen.dim_4dp))
                    .clickable { onHelpClick() },
                tint = Color.Gray
            )
        }
    }
}

// Preview helper function
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

@Preview
@Composable
fun PreviewAsteroidDetail() {
    AsteroidDetail(
        asteroidModel = getDummyAsteroid(),
        modifier = Modifier.background(Color.Black),
        onHelpClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AsteroidDetailScreenPreview() {
    AsteroidDetailScreen(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        asteroidModel = getDummyAsteroid(),
        isLoading = false,
        isError = false,
        onNavigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AsteroidDetailScreenLoadingPreview() {
    AsteroidDetailScreen(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        asteroidModel = null,
        isLoading = true,
        isError = false,
        onNavigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AsteroidDetailScreenErrorPreview() {
    AsteroidDetailScreen(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        asteroidModel = null,
        isLoading = false,
        isError = true,
        onNavigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AsteroidDetailScreenHazardousPreview() {
    AsteroidDetailScreen(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        asteroidModel = getDummyAsteroid().copy(isPotentiallyHazardous = true),
        isLoading = false,
        isError = false,
        onNavigateBack = {}
    )
}

@Composable
fun AstronomicalUnitExplanationDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDismiss() }) {
            Text(text = stringResource(android.R.string.ok))
        }
    }, title = { Text(text = stringResource(R.string.text_astronomical_unit_explanation)) })
}