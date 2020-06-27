package com.surya.canadaholidays.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvinceAPI {

    @GET("provinces")
    fun getProvincesList(@Query("year") year: Int?):Single<ProvinceModel>

    @GET("provinces/{provinceCode}")
    fun getHolidayList(@Path("provinceCode")provinceCode: String? ,@Query("year") year: Int?): Single<HolidayModel>
}