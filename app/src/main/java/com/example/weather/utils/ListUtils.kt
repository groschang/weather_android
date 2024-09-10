package com.example.weather.utils


fun <T> List<T>.findAdjacent(target: T, before: Boolean = true): T? {
    val index = this.indexOf(target)
    return if (index != -1) {
        if (before) {
            this.getOrNull(index - 1) ?: this.getOrNull(index + 1)
        } else {
            this.getOrNull(index + 1) ?: this.getOrNull(index - 1)
        }
    } else {
        null
    }
}