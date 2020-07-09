package com.surya.canadaholidays

import com.surya.canadaholidays.di.ProvinceAPIModule
import com.surya.canadaholidays.model.ProvinceAPIService

open class ProvinceAPIModuleTest(private val mockProvinceAPIService: ProvinceAPIService) : ProvinceAPIModule() {

    fun getProvinceApiService(): ProvinceAPIService {
        return mockProvinceAPIService
    }
}