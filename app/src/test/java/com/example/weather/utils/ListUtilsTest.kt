package com.example.weather.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ListExtensionsTest {

    @Test
    fun `findAdjacent returns null for empty list`() {
        // Given
        val list = emptyList<Int>()
        val target = 1

        // When
        val result = list.findAdjacent(target)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun `findAdjacent returns null if target not found`() {
        // Given
        val list = listOf(1, 2, 3)
        val target = 4

        // When
        val result = list.findAdjacent(target)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun `findAdjacent returns before element if present`() {
        // Given
        val list = listOf(1, 2, 3)
        val target = 2

        // When
        val result = list.findAdjacent(target)

        // Then
        assertEquals(1, result)
    }

    @Test
    fun `findAdjacent returns after element if before not present`() {
        // Given
        val list = listOf(1, 2, 3)
        val target = 1

        // When
        val result = list.findAdjacent(target)

        // Then
        assertEquals(2, result)
    }

    @Test
    fun `findAdjacent returns after element when before is false`() {
        // Given
        val list = listOf(1, 2, 3)
        val target = 2

        // When
        val result = list.findAdjacent(target, before = false)

        // Then
        assertEquals(3, result)
    }

    @Test
    fun `findAdjacent returns before element if after not present and before is false`() {
        // Given
        val list = listOf(1, 2, 3)
        val target = 3

        // When
        val result = list.findAdjacent(target, before = false)

        // Then
        assertEquals(2, result)
    }
}