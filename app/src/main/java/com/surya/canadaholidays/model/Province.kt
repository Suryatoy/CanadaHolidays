package com.surya.canadaholidays.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Province(
    val holidays: List<Holiday>,
    val id: String,
    val nameEn: String,
    val nameFr: String,
    val nextHoliday: NextHoliday,
    val sourceEn: String,
    val sourceLink: String
):Parcelable