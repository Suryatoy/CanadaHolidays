package com.surya.canadaholidays.model

import com.surya.canadaholidays.di.DaggerProvinceAPIComponent
import io.reactivex.Single
import javax.inject.Inject

class ProvinceAPIService {

    @Inject
    lateinit var api: ProvinceAPI

    init {
        DaggerProvinceAPIComponent.create().inject(this)
    }

    fun getProvinces(year: Int?): Single<ProvinceModel> {
        return api.getProvincesList(year)
    }

    fun getHolidays(provinceCOde: String?, year: Int?): Single<HolidayModel> {
        return api.getHolidayList(provinceCOde, year)
    }
}