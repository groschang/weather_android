package com.conrad.weather.ui.common.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun WeatherIconButton(
    weatherIcon: WeatherAppIcon,
    color: Color? = null,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = weatherIcon.imageVector,
            contentDescription = stringResource(id = weatherIcon.imageName),
            tint = color ?: MaterialTheme.colorScheme.onSurface
        )
    }
}