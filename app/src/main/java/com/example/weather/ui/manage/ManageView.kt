package com.example.weather.ui.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.data.AppContainerMock
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.cities.entity.toDBCity
import com.example.weather.model.stubs.CitiesStub
import com.example.weather.ui.AppViewModelProvider
import com.example.weather.ui.common.components.AddAppIcon
import com.example.weather.ui.common.components.BackAppIcon
import com.example.weather.ui.common.components.WeatherIconButton
import com.example.weather.ui.common.components.WeatherTopAppBar
import com.example.weather.ui.common.viewstates.LoadingScreen
import com.example.weather.ui.common.viewstates.NoResultsScreen
import com.example.weather.ui.navigation.NavigationDestination
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.utils.Result


object ManageDestination : NavigationDestination {
    override val route = "manage"
    override val titleRes = R.string.manage
    const val itemArg = "itemObject"
    val routeWithArgs = "$route/{$itemArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageView(
    navigateBack: () -> Unit,
    navigateToDetail: () -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ManageViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            WeatherTopAppBar(
                title = stringResource(R.string.manage_locations),
                icon = BackAppIcon,
                onClick = navigateBack,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (viewModel.canAddCity) {
                        WeatherIconButton(
                            weatherIcon = AddAppIcon,
                            onClick = navigateToSearch
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val uiState = viewModel.uiState

            when (uiState) {
                is Result.Loading -> LoadingScreen(
                    modifier = modifier.fillMaxSize()
                )

                is Result.Success -> CitiesListView(
                    cities = uiState.data,
                    selectedCityId = viewModel.selectedCityId,
                    onItemClick = {
                        viewModel.selectCity(it)
                        navigateToDetail()
                    },
                    onDeleteItemClick = viewModel::removeCity,
                    modifier = modifier.fillMaxSize()
                )

                else -> NoResultsScreen(
                    message = stringResource(R.string.search_new_location),
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CitiesListView(
    cities: List<DBCity>,
    selectedCityId: String?,
    onItemClick: (DBCity) -> Unit,
    onDeleteItemClick: (DBCity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(cities) { city ->
            CityItemView(
                text = city.name,
                description = city.location,
                selected = city.id == selectedCityId,
                onItemClick = { onItemClick(city) },
                onDeleteItemClick = { onDeleteItemClick(city) }
            )
        }
    }
}

@Composable
fun CityItemView(
    text: String,
    description: String,
    selected: Boolean,
    onItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 0.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }

        val composableWidth = LocalView.current.width.dp
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = composableWidth - 16.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = !expanded
                    onDeleteItemClick()
                },
                text = {
                    Text(stringResource(R.string.remove_location))
                }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ManageViewPreview() {
    var container = AppContainerMock(LocalContext.current)
    var viewModel = ManageViewModel(
        container.weatherRepository,
        container.settingsRepository,
        SavedStateHandle()
    )
    WeatherTheme {
        ManageView(
            navigateBack = { },
            navigateToSearch = { },
            navigateToDetail = { },
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CitiesListViewPreview(darkTheme: Boolean = false) {
    val cities = CitiesStub.cities.map { it.toDBCity() }
    WeatherTheme(darkTheme = darkTheme) {
        CitiesListView(
            cities = cities,
            selectedCityId = cities[0].id,
            onItemClick = { },
            onDeleteItemClick = { }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CitiesListViewDarkPreview() {
    CitiesListViewPreview(darkTheme = true)
}
