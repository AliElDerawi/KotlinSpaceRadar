package com.udacity.asteroidradar.features.main.view

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.bundle.Bundle
import com.udacity.asteroidradar.theme.AsteroidRadarTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsteroidRadarTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(WindowInsets.navigationBars.asPaddingValues()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AsteroidApp()
                }
            }
        }
    }
}