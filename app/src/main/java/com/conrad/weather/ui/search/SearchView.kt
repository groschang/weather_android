package com.conrad.weather.ui.search

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.conrad.weather.R
import com.conrad.weather.model.City
import com.conrad.weather.model.location
import com.conrad.weather.ui.common.components.SearchTopAppBar
import com.conrad.weather.ui.common.viewstates.ErrorScreen
import com.conrad.weather.ui.common.viewstates.IdleScreen
import com.conrad.weather.ui.common.viewstates.LoadingScreen
import com.conrad.weather.ui.common.viewstates.NoResultsScreen
import com.conrad.weather.ui.navigation.NavigationDestination
import com.conrad.weather.ui.theme.WeatherTheme
import com.conrad.weather.utils.Result

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.search
}

@Composable
fun SearchView(
    navigateBack: () -> Unit,
    onSelectCity: (City) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
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
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
    }
}


@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    WeatherTheme {
        SearchView(
            navigateBack = {},
            onSelectCity = {}
        )
    }
}