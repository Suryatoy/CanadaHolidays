package com.surya.canadaholidays.util

import java.util.*

class GenericUtils {

    companion object {
        fun getYear(): Int? {
            return Calendar.getInstance().get(Calendar.YEAR)
        }
    }
}