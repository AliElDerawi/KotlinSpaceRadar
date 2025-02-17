package com.udacity.asteroidradar.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun dimenToSp(id: Int): TextUnit {
    return dimensionResource(id = id).value.sp
}
