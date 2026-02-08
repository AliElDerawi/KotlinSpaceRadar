package com.udacity.asteroidradar.features.main.view

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.navigation.AsteroidNavHost

@Composable
fun AsteroidApp(navController: NavHostController = rememberNavController()) {
    AsteroidNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidAppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onFilterClick: (AsteroidApiFilter) -> Unit = {},
    canNavigateBack: Boolean,
    showMenu: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.text_back_button)
                    )
                }
            }
        },
        actions = {
            if (showMenu) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = stringResource(R.string.text_menu_options)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.text_filter_today)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_TODAY)
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.text_filter_week)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_WEEK)
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.text_filter_saved)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_SAVED)
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        })
}