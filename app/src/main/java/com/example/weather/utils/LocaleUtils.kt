package com.example.weather.utils

import androidx.compose.ui.text.intl.Locale


val Locale.Companion.language: String
    get() = current.toLanguageTag()
