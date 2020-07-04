package com.surya.canadaholidays.view.interfaces

import android.view.View
import com.surya.canadaholidays.model.Holiday

interface HolidayClickListener {
    fun onItemClick(view: View,holiday:Holiday)
}