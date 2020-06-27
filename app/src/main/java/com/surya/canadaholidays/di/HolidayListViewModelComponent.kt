package com.surya.canadaholidays.di

import com.surya.canadaholidays.viewmodel.HolidayListViewModel
import dagger.Component

@Component(modules = [ProvinceAPIModule::class])
interface HolidayListViewModelComponent {

    fun inject(holidayListViewModel: HolidayListViewModel)
}