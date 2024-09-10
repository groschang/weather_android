package com.example.weather.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.R
import com.example.weather.ui.common.components.MenuAppIcon
import com.example.weather.ui.common.components.WeatherTopAppBar
import com.example.weather.ui.detail.DetailView
import com.example.weather.ui.drawer.DrawerView
import com.example.weather.ui.navigation.NavigationDestination
import com.example.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navigateToEditLocations: () -> Unit,
    navigateToAddLocation: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    ModalNavigationDrawer(
        drawerContent = {
            DrawerView(
                navigateToEditLocations = navigateToEditLocations,
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                WeatherTopAppBar(
                    title = stringResource(R.string.app_name),
                    icon = MenuAppIcon,
                    onClick = { coroutineScope.launch { drawerState.open() } },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            DetailView(
                navigateToAddLocation = navigateToAddLocation,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
    WeatherTheme {
        HomeView(
            navigateToEditLocations = { },
            navigateToAddLocation = { },
        )
    }
}

