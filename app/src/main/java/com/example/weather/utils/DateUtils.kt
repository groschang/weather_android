package com.example.weather.utils


import android.content.Context
import com.example.weather.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


object DateFormatter {

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    private val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)

    fun format(date: LocalDate, context: Context): String {
        val isToday = date == LocalDate.now()

        return if (isToday) {
            context.getString(R.string.today)
        } else {
            date.format(formatter)
        }
    }

    fun format(date: Date, context: Context): String {
        return format(dateToLocalDate(date), context)
    }

    fun dateToLocalDate(date: Date): LocalDate {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}

fun Date.toDayString(context: Context): String {
    return DateFormatter.format(this, context)
}