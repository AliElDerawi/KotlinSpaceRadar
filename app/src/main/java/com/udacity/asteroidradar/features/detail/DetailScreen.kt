package com.udacity.asteroidradar.features.detail

import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.features.main.view.AsteroidAppTopBar
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
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
    viewModel: MainViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            AsteroidAppTopBar(
                title = stringResource(AsteroidDetailDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, modifier = modifier
    ) { innerPadding ->
        AsteroidDetail(
            asteroid = AsteroidModel(
                isPotentiallyHazardous = false,
                closeApproachDate = "2020-02-01",
                absoluteMagnitude = 25.126,
                estimatedDiameter = 0.82,
                relativeVelocity = 11.9,
                distanceFromEarth = 0.0924,
                id = 1,
                codename = stringResource(R.string.app_name)
            ), onHelpClick = { /* Do something */ }, modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun AsteroidDetail(
    asteroid: AsteroidModel, onHelpClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
    ) {
        // Image
        Image(
            painter = painterResource(
                if (asteroid.isPotentiallyHazardous) R.drawable.asteroid_hazardous
                else R.drawable.asteroid_safe
            ),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        // "Close approach date"
        DetailItem(
            title = stringResource(R.string.text_close_approach_date),
            value = asteroid.closeApproachDate,
            modifier = modifier
        )

        // "Absolute magnitude"
        DetailItem(
            title = stringResource(R.string.text_absolute_magnitude),
            value = "${asteroid.absoluteMagnitude} au",
            helpIcon = true,
            onHelpClick = onHelpClick,
            modifier = modifier
        )

        // "Estimated diameter"
        DetailItem(
            title = stringResource(R.string.text_estimated_diameter),
            value = "${asteroid.estimatedDiameter} km",
            modifier = modifier
        )

        // "Relative velocity"
        DetailItem(
            title = stringResource(R.string.text_relative_velocity),
            value = "${asteroid.relativeVelocity} km/s",
            modifier = modifier
        )

        // "Distance from earth"
        DetailItem(
            title = stringResource(R.string.text_distance_from_earth),
            value = "${asteroid.distanceFromEarth} au",
            modifier = modifier
        )
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns all children vertically centered
    ) {
        // Column for title and value texts
        Column(
            modifier = modifier.weight(1f) // Text takes up available horizontal space
        ) {
            Text(
                text = title,
                fontSize = dimenToSp(R.dimen.text_normal),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp)) // Spacing between title and value
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
                contentDescription = "Help Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
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
fun displayAstronomicalUnitExplanationDialog() {
    val builder = AlertDialog.Builder(LocalContext.current)
        .setMessage(R.string.text_astronomical_unit_explanation)
        .setPositiveButton(android.R.string.ok, null)
    builder.create().show()
}