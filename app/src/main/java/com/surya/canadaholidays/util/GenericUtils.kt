package com.surya.canadaholidays.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * To get current year
 */
fun getYear(): Int? {
    return Calendar.getInstance().get(Calendar.YEAR)
}

/**
 * To convert date string to easy readable format
 */
fun convertTimestamp(updatedDate: String?): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd MMM yyyy")
    return formatter.format(parser.parse(updatedDate))
}

