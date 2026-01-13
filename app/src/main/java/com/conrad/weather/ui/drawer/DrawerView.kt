package com.conrad.weather.ui.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.conrad.weather.R
import com.conrad.weather.ui.theme.WeatherTheme
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DrawerView(
    navigateToEditLocations: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DrawerViewModel = hiltViewModel(),
) {
    ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
        DrawerHeader(modifier)
        Spacer(modifier = Modifier.padding(5.dp))

        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier.padding(horizontal = 4.dp)
        ) {
            items(viewModel.cities) { city ->
                LocationItemView(
                    name = city.name,
                    selected = viewModel.selectedCityId == city.id,
                    onClick = {
                        viewModel.selectCity(city)
                        closeDrawer()
                    }
                )
            }
        }

        ManageLocationItemView(
            onClick = {
                navigateToEditLocations()
                closeDrawer()
            },
            modifier = modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun LocationItemView(
    name: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium
            )
        },
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null
            )
        },
        shape = MaterialTheme.shapes.small
    )
}

@Composable
fun ManageLocationItemView(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.manage_location),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrawerViewPreview() {
    WeatherTheme {
        DrawerView(
            navigateToEditLocations = { },
            closeDrawer = { },
            viewModel = DrawerViewModelFactory.createMock()
        )
    }
}