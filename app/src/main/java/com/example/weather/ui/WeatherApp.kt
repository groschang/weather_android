package com.example.weather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.navigation.WeatherNavHost
import com.example.weather.ui.theme.WeatherTheme

@Composable
fun WeatherApp(
    navController: NavHostController = rememberNavController()
) {
    WeatherTheme {
        WeatherNavHost(navController = navController)
    }
}
