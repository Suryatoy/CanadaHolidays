package com.surya.canadaholidays.di

import com.surya.canadaholidays.model.ProvinceAPI
import com.surya.canadaholidays.model.ProvinceAPIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ProvinceAPIModule {

    private val BASE_URL = "https://canada-holidays.ca/api/v1/"

    @Provides
    fun provideProvinceAPI(): ProvinceAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ProvinceAPI::class.java)
    }

    @Provides
    fun provideProvinceAPIService():ProvinceAPIService{
        return ProvinceAPIService()
    }
}