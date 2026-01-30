package com.udacity.asteroidradar.features.detail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.features.detail.viewModel.DetailScreenViewModel
import com.udacity.asteroidradar.features.main.view.AsteroidAppTopBar
import com.udacity.asteroidradar.util.dimenToSp
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

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
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = koinViewModel()
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        AsteroidAppTopBar(
            title = viewModel.asteroidModel?.codename
                ?: stringResource(AsteroidDetailDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateBack,
            showMenu = false
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            viewModel.asteroidModel?.let { asteroid ->
                AsteroidDetail(
                    asteroidModel = asteroid,
                    onHelpClick = { showDialog = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    if (showDialog) {
        AstronomicalUnitExplanationDialog(onDismiss = { showDialog = false })
    }
}

@Composable
fun AsteroidDetail(
    modifier: Modifier = Modifier, asteroidModel: AsteroidModel, onHelpClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
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

            Column(modifier = Modifier.padding(dimensionResource(R.dimen.dim_default_margin))) {
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
                    title = stringResource(R.string.text_relative_velocity), value = stringResource(
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
}

@Composable
fun DetailItem(
    title: String,
    value: String,
    helpIcon: Boolean = false,
    onHelpClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.dim_6dp)),
        verticalAlignment = Alignment.CenterVertically // Aligns all children vertically centered
    ) {
        // Column for title and value texts
        Column(
            modifier = Modifier.weight(1f) // Text takes up available horizontal space
        ) {
            Text(
                text = title,
                fontSize = dimenToSp(R.dimen.text_normal),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_4dp))) // Spacing between title and value

            Text(
                text = value,
                fontSize = dimenToSp(R.dimen.text_small),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        // Helper icon
        if (helpIcon && onHelpClick != null) {
            Icon(
                painter = painterResource(R.drawable.ic_help_circle),
                contentDescription = stringResource(R.string.text_description_help_icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(dimensionResource(R.dimen.dim_4dp))
                    .clickable { onHelpClick() }, // Click behavior
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
        modifier = Modifier.background(Color.Black),
        asteroidModel = getDummyAsteroid(),
        onHelpClick = {}
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