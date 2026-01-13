package com.conrad.weather.ui.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector
import com.conrad.weather.R

interface WeatherAppIcon {
    val imageVector: ImageVector
    val imageName: Int
}


object BackAppIcon : WeatherAppIcon {
    override val imageVector = Icons.AutoMirrored.Rounded.ArrowBack
    override val imageName = R.string.back_button
}

object MenuAppIcon : WeatherAppIcon {
    override val imageVector = Icons.Filled.Menu
    override val imageName = R.string.menu
}

object AddAppIcon : WeatherAppIcon {
    override val imageVector = Icons.Default.Add
    override val imageName = R.string.add
}

object CloseAppIcon : WeatherAppIcon {
    override val imageVector = Icons.Rounded.Close
    override val imageName = R.string.clear
}



