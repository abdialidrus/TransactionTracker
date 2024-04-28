package com.abdialidrus.nuta.transactiontracker.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.convertToDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.convertToTimeString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

fun Long?.formatWithDefaultDelimiter(): String =
    NumberFormat.getNumberInstance(Locale.getDefault()).format(this ?: 0)

fun Int?.formatWithDefaultDelimiter(): String =
    NumberFormat.getNumberInstance(Locale.getDefault()).format(this ?: 0)

fun String.toTimeMillis(pattern: String): Long {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val date = dateFormat.parse(this)
    return date?.time ?: 0L
}