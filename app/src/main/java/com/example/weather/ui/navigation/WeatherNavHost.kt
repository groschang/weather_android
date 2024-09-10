package com.example.weather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weather.ui.home.HomeDestination
import com.example.weather.ui.home.HomeView
import com.example.weather.ui.manage.ManageDestination
import com.example.weather.ui.manage.ManageView
import com.example.weather.ui.search.SearchDestination
import com.example.weather.ui.search.SearchView
import com.google.gson.Gson


@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {

        composable(route = HomeDestination.route) {
            HomeView(
                navigateToEditLocations = {
                    navController.navigate(ManageDestination.route)
                },
                navigateToAddLocation = {
                    navController.navigate(ManageDestination.route) {
                        popUpTo(HomeDestination.route)
                    }
                    navController.navigate(SearchDestination.route) {
                        popUpTo(ManageDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = SearchDestination.route) {
            SearchView(
                navigateBack = navController::navigateUp,
                onSelectCity = { city ->
                    val cityJson = Gson().toJson(city)
                    navController.navigate("${ManageDestination.route}/$cityJson") {
                        popUpTo(HomeDestination.route)
                    }
                }
            )
        }

        composable(route = ManageDestination.route) {
            ManageView(
                navigateBack = navController::navigateUp,
                navigateToSearch = {
                    navController.navigate(SearchDestination.route) {
                        launchSingleTop = true
                        popUpTo(ManageDestination.route)
                    }
                },
                navigateToDetail = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(
            route = ManageDestination.routeWithArgs,
            arguments = listOf(navArgument(ManageDestination.itemArg) {
                type = NavType.StringType
            })
        ) {
            ManageView(
                navigateBack = navController::navigateUp,
                navigateToSearch = {
                    navController.navigate(SearchDestination.route) {
                        launchSingleTop = true
                        popUpTo(ManageDestination.route)
                    }
                },
                navigateToDetail = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}