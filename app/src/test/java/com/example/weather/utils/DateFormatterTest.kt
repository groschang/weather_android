package com.example.weather.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class DateFormatterTest {

    private lateinit var context: Context
    private val today = LocalDate.now()
    private val tomorrow = today.plusDays(1)

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `format LocalDate returns day of week for other dates`() {
        // Given
        val expectedDayOfWeek = tomorrow.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))

        // When
        val formattedDate = DateFormatter.format(tomorrow, context)

        // Then
        assertEquals(expectedDayOfWeek, formattedDate)
    }

    @Test
    fun `format Date returns day of week for other dates`() {
        // Given
        val date = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val expectedDayOfWeek = tomorrow.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))

        // When
        val formattedDate = DateFormatter.format(date, context)

        // Then
        assertEquals(expectedDayOfWeek, formattedDate)
    }

    @Test
    fun `dateToLocalDate converts Date to LocalDate correctly`() {
        // Given
        val date = Date()
        val expectedLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        // When
        val localDate = DateFormatter.dateToLocalDate(date)

        // Then
        assertEquals(expectedLocalDate, localDate)
    }

    @Test
    fun `Date toDayString extension returns day of week for other dates`() {
        // Given
        val date = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val expectedDayOfWeek = tomorrow.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))

        // When
        val formattedDate = date.toDayString(context)

        // Then
        assertEquals(expectedDayOfWeek, formattedDate)
    }
}
