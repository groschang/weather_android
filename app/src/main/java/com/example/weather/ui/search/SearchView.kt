package com.example.weather.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.data.AppContainerMock
import com.example.weather.model.City
import com.example.weather.model.locationDescription
import com.example.weather.model.stubs.CitiesStub
import com.example.weather.ui.AppViewModelProvider
import com.example.weather.ui.common.components.SearchTopAppBar
import com.example.weather.ui.common.viewstates.ErrorScreen
import com.example.weather.ui.common.viewstates.IdleScreen
import com.example.weather.ui.common.viewstates.LoadingScreen
import com.example.weather.ui.common.viewstates.NoResultsScreen
import com.example.weather.ui.navigation.NavigationDestination
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.utils.Result


object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.search
}

@Composable
fun SearchView(
    navigateBack: () -> Unit,
    onSelectCity: (City) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        SearchToolbar(
            onBackClick = navigateBack,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onSearchTriggered = viewModel::onSearchTriggered,
            searchQuery = viewModel.searchQuery,
        )

        val uiState = viewModel.uiState

        when (uiState) {
            is Result.Loading -> LoadingScreen(
                modifier = modifier.fillMaxSize()
            )

            is Result.Error -> ErrorScreen(
                message = stringResource(R.string.loading_failed),
                retryAction = viewModel::retry,
                modifier = modifier.fillMaxSize()
            )

            is Result.Success -> SearchListView(
                cities = uiState.data,
                onItemClick = onSelectCity,
                modifier = modifier.fillMaxWidth()
            )

            is Result.NoResults -> NoResultsScreen(
                message = "Search new location",
                modifier = modifier.fillMaxSize()
            )

            else -> IdleScreen(
                message = stringResource(R.string.search_new_location)
            )
        }
    }
}

@Composable
fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val windowInfo = LocalWindowInfo.current
    val focusRequester = remember { FocusRequester() }

    SearchTopAppBar(
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        onSearchTriggered = onSearchTriggered,
        onBackClick = onBackClick,
        focusRequester = focusRequester,
        modifier = modifier,
        keyboardController = keyboardController
    )

    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }
    }
}

@Composable
fun SearchListView(
    cities: List<City>,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(cities) { city ->
            CityItem(
                city = city,
                modifier = modifier
                    .padding()
                    .clickable { onItemClick(city) }
            )

        }
    }
}

@Composable
fun CityItem(
    city: City,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = city.locationDescription(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchViewPreview() {
    var container = AppContainerMock(LocalContext.current)
    var viewModel = SearchViewModel(container.weatherRepository)
    viewModel.uiState = Result.Success(CitiesStub.cities)
    WeatherTheme(darkTheme = true) {
        SearchView(
            navigateBack = { },
            onSelectCity = { },
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CitiesListScreenPreview() {
    WeatherTheme {
        val mockData = CitiesStub.cities
        SearchListView(
            cities = mockData,
            onItemClick = { }
        )
    }
}