package com.conrad.weather.ui.manage

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
import com.conrad.weather.R
import com.conrad.weather.model.City
import com.conrad.weather.model.location
import com.conrad.weather.model.stubs.CitiesStub
import com.conrad.weather.ui.common.components.AddAppIcon
import com.conrad.weather.ui.common.components.BackAppIcon
import com.conrad.weather.ui.common.components.WeatherIconButton
import com.conrad.weather.ui.common.components.WeatherTopAppBar
import com.conrad.weather.ui.common.viewstates.LoadingScreen
import com.conrad.weather.ui.common.viewstates.NoResultsScreen
import com.conrad.weather.ui.navigation.NavigationDestination
import com.conrad.weather.ui.theme.WeatherTheme
import com.conrad.weather.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel

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
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ManageViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
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

                is Result.Error -> {
                    // Handle error state
                }

                is Result.Success -> ManageListView(
                    cities = uiState.data,
                    selectedCityId = viewModel.selectedCityId,
                    onSelectCity = viewModel::selectCity,
                    onRemoveCity = viewModel::removeCity,
                    modifier = modifier.fillMaxSize()
                )

                is Result.NoResults -> NoResultsScreen(
                    message = stringResource(R.string.search_new_location),
                    modifier = modifier.fillMaxSize()
                )

                else -> {
                    // Handle idle state
                }
            }
        }
    }
}

@Composable
fun ManageListView(
    cities: List<City>,
    selectedCityId: String?,
    onSelectCity: (City) -> Unit,
    onRemoveCity: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(cities) { city ->
            ManageItemView(
                city = city,
                selected = selectedCityId == city.id,
                onSelect = onSelectCity,
                onRemove = onRemoveCity
            )
        }
    }
}

@Composable
fun ManageItemView(
    city: City,
    selected: Boolean,
    onSelect: (City) -> Unit,
    onRemove: (City) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val view = LocalView.current

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect(city) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = city.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = city.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.menu)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(
                        x = 16.dp,
                        y = (-48).dp
                    )
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.remove_location))
                        },
                        onClick = {
                            onRemove(city)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageViewPreview() {
    WeatherTheme {
        ManageView(
            navigateBack = {},
            navigateToSearch = {}
        )
    }
}