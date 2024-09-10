package com.example.weather.ui.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeatherTheme


@Composable
fun SearchTopAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        WeatherIconButton(
            weatherIcon = BackAppIcon,
            onClick = onBackClick
        )

        SearchTextField(
            onSearchQueryChange = onSearchQueryChange,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SearchTopAppBarPreview() {
    WeatherTheme {
        SearchTopAppBar(
            searchQuery = "",
            onSearchQueryChange = { },
            onSearchTriggered = { },
            onBackClick = { }
        )
    }
}