package com.surya.canadaholidays.application

import android.app.Application
import android.content.Context



class CanadaHolidaysApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}