package com.conrad.weather.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.conrad.weather.R
import com.conrad.weather.model.City
import com.conrad.weather.model.location
import com.conrad.weather.model.DailyForecast
import com.conrad.weather.model.Headline
import com.conrad.weather.model.WeatherForecast
import com.conrad.weather.repository.WeatherRepositoryMock
import com.conrad.weather.local.settings.DataStoreSettingsRepositoryMock
import com.conrad.weather.ui.common.viewstates.ErrorScreen
import com.conrad.weather.ui.common.viewstates.LoadingScreen
import com.conrad.weather.ui.common.viewstates.NoResultsScreen
import com.conrad.weather.ui.theme.WeatherTheme
import com.conrad.weather.utils.Result
import com.conrad.weather.utils.toDayString
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailView(
    navigateToAddLocation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
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
            modifier = modifier.fillMaxSize(),
        )

        is Result.NoResults -> NoResultsScreen(
            message = stringResource(R.string.search_new_location),
            modifier = modifier.fillMaxSize()
        )

        else -> AddLocationView(
            navigateToAddLocation = navigateToAddLocation,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun DetailContentView(
    weatherForecast: WeatherForecast?,
    modifier: Modifier = Modifier,
    city: City? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        if (city != null) {
            item {
                CityView(city = city)
            }
        }

        weatherForecast?.let { forecast ->
            item {
                HeadlineView(headline = forecast.headline)
            }

            items(forecast.dailyForecasts) { dailyForecast ->
                DailyForecastItemView(dailyForecast = dailyForecast)
            }
        }
    }
}

@Composable
fun CityView(city: City) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = city.location,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HeadlineView(headline: Headline) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = headline.text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun DailyForecastItemView(dailyForecast: DailyForecast) {
    val context = LocalContext.current
    val dayString = dailyForecast.date.toDayString(context)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = dayString,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = dailyForecast.day.iconPhrase,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${dailyForecast.temperature.maximum.value}°",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${dailyForecast.temperature.minimum.value}°",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AddLocationView(
    navigateToAddLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(48.dp)
                .scale(1.5f)
        )
        Text(
            text = stringResource(R.string.add_location),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = navigateToAddLocation,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_location),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DetailViewPreview() {
    WeatherTheme {
        DetailView(
            navigateToAddLocation = {},
            viewModel = DetailViewModel(
                weatherRepository = WeatherRepositoryMock(),
                settingsRepository = DataStoreSettingsRepositoryMock()
            )
        )
    }
}