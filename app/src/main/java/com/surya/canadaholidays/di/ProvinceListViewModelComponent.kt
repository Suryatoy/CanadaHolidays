package com.surya.canadaholidays.di

import com.surya.canadaholidays.viewmodel.ProvinceListViewModel
import dagger.Component

@Component(modules = [ProvinceAPIModule::class])
interface ProvinceListViewModelComponent {

    fun inject(provinceListViewModel: ProvinceListViewModel)
}