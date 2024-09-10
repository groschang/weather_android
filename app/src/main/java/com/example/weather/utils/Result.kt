package com.example.weather.utils


sealed class Result<out R> {

    object Idle : Result<Nothing>()

    object Loading : Result<Nothing>()

    data class Error(val exception: Exception) : Result<Nothing>()

    object NoResults : Result<Nothing>()

    data class Success<out T>(val data: T) : Result<T>()
}