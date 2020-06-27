package com.surya.canadaholidays.di

import com.surya.canadaholidays.model.ProvinceAPIService
import dagger.Component

@Component(modules = [ProvinceAPIModule::class])
interface ProvinceAPIComponent {

    fun inject(service:ProvinceAPIService)
}