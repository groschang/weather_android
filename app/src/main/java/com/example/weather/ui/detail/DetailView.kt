package com.example.weather.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.data.AppContainerMock
import com.example.weather.local.cities.entity.DBCity
import com.example.weather.local.cities.entity.toDBCity
import com.example.weather.model.DailyForecast
import com.example.weather.model.Headline
import com.example.weather.model.WeatherForecast
import com.example.weather.model.stubs.CitiesStub
import com.example.weather.model.stubs.WeatherForecastStub
import com.example.weather.ui.AppViewModelProvider
import com.example.weather.ui.common.viewstates.ErrorScreen
import com.example.weather.ui.common.viewstates.LoadingScreen
import com.example.weather.ui.common.viewstates.NoResultsScreen
import com.example.weather.ui.theme.LocalDomainColors
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.utils.Result
import com.example.weather.utils.toDayString


@Composable
fun DetailView(
    navigateToAddLocation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Box(modifier = modifier) {

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

            is Result.Success -> DetailContentView(
                city = viewModel.city,
                weatherForecast = uiState.data,
                navigateToAddLocation = navigateToAddLocation,
                modifier = modifier.fillMaxSize(),
            )

            is Result.NoResults -> NoResultsScreen(
                message = stringResource(R.string.search_new_location),
                modifier = modifier.fillMaxSize()
            )

            else -> AddLocationView(
                navigateToAddLocation = navigateToAddLocation,
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            )
        }
    }
}

@Composable
fun DetailContentView(
    weatherForecast: WeatherForecast?,
    navigateToAddLocation: () -> Unit,
    modifier: Modifier = Modifier,
    city: DBCity? = null,
) {
    if (weatherForecast != null) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
        ) {
            city?.let {
                CityView(city = it)
            }
            weatherForecast.dailyForecasts.first().let {
                ForecastView(forecast = it)
            }
            HeadlineView(headline = weatherForecast.headline)
            TemperaturesView(forecasts = weatherForecast.dailyForecasts)
        }
    } else {
        AddLocationView(navigateToAddLocation = navigateToAddLocation)
    }
}

@Composable
fun CityView(
    city: DBCity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = city.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ForecastView(
    forecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = forecast.temperature.minimum.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                color = LocalDomainColors.current.cold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = forecast.day.iconPhrase,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = LocalDomainColors.current.cold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 15.dp)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = forecast.temperature.maximum.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                color = LocalDomainColors.current.hot,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = forecast.night.iconPhrase,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = LocalDomainColors.current.hot,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun HeadlineView(
    headline: Headline,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = headline.text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
fun TemperaturesView(
    forecasts: List<DailyForecast>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        items(forecasts) { forecast ->
            ForecastItem(forecast)
        }
    }
}

@Composable
fun ForecastItem(
    forecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = forecast.date.toDayString(LocalContext.current),
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = forecast.temperature.toString(),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun AddLocationView(
    navigateToAddLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.add_location),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = navigateToAddLocation,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.scale(1.5f)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailViewPreview(darkTheme: Boolean = false) {
    var container = AppContainerMock(LocalContext.current)
    var viewModel = DetailViewModel(container.weatherRepository, container.settingsRepository)
    viewModel.city = CitiesStub.cities.first().toDBCity()
    viewModel.uiState = Result.Success(WeatherForecastStub.weatherForecast)
    WeatherTheme(darkTheme = darkTheme) {
        DetailView(
            navigateToAddLocation = { },
            viewModel = viewModel,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailViewDarkPreview(darkTheme: Boolean = false) {
    DetailViewPreview(darkTheme = true)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddLocationViewPreview() {
    WeatherTheme {
        AddLocationView(
            navigateToAddLocation = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}