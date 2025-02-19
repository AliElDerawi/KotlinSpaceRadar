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
import androidx.compose.runtime.remember
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
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.features.detail.viewModel.DetailScreenViewModel
import com.udacity.asteroidradar.features.main.view.AsteroidAppTopBar
import com.udacity.asteroidradar.navigation.NavigationDestination
import com.udacity.asteroidradar.util.dimenToSp
import org.koin.androidx.compose.koinViewModel

object AsteroidDetailDestination : NavigationDestination {
    override val route = "detail"
    override val titleRes = R.string.text_asteroid_detail
    const val ASTEROID_MODEL_ARG = "asteroidModel"
    val routeWithArgs = "$route/{$ASTEROID_MODEL_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        AsteroidAppTopBar(
            title = viewModel.asteroidModel?.codename
                ?: stringResource(R.string.text_asteroid_detail),
            canNavigateBack = true,
            navigateUp = navigateBack
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            viewModel.asteroidModel?.let { asteroid ->
                AsteroidDetail(
                    asteroid = asteroid,
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
    modifier: Modifier = Modifier, asteroid: AsteroidModel, onHelpClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            // Image
            Image(
                painter = painterResource(
                    if (asteroid.isPotentiallyHazardous) R.drawable.asteroid_hazardous
                    else R.drawable.asteroid_safe
                ),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_small_margin)))

            // Details

            Column(modifier = Modifier.padding(dimensionResource(R.dimen.dim_default_margin))) {
                DetailItem(
                    title = stringResource(R.string.text_close_approach_date),
                    value = asteroid.closeApproachDate
                )

                DetailItem(
                    title = stringResource(R.string.text_absolute_magnitude),
                    value = stringResource(
                        R.string.text_format_astronomical_unit, asteroid.absoluteMagnitude
                    ),
                    helpIcon = true,
                    onHelpClick = onHelpClick
                )

                DetailItem(
                    title = stringResource(R.string.text_estimated_diameter),
                    value = stringResource(
                        R.string.text_format_km_unit, asteroid.estimatedDiameter
                    )
                )

                DetailItem(
                    title = stringResource(R.string.text_relative_velocity), value = stringResource(
                        R.string.text_format_km_s_unit, asteroid.relativeVelocity
                    )
                )

                DetailItem(
                    title = stringResource(R.string.text_distance_from_earth),
                    value = stringResource(
                        R.string.text_format_astronomical_unit, asteroid.distanceFromEarth
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

@Preview
@Composable
fun PreviewAsteroidDetailScreen() {
//    AsteroidDetailScreen(
//        asteroid = AsteroidModel(
//            isPotentiallyHazardous = false,
//            closeApproachDate = "2020-02-01",
//            absoluteMagnitude = 25.126,
//            estimatedDiameter = 0.82,
//            relativeVelocity = 11.9,
//            distanceFromEarth = 0.0924,
//            id = 1,
//            codename = stringResource(R.string.app_name)
//        ),
//        onHelpClick = { /* Do something */ },
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    )
}

@Composable
fun AstronomicalUnitExplanationDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDismiss() }) {
            Text(text = stringResource(android.R.string.ok))
        }
    }, title = { Text(text = stringResource(R.string.text_astronomical_unit_explanation)) })
}