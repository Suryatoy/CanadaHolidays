package com.surya.canadaholidays.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NextHoliday(
    val date: String,
    val federal: Int,
    val id: Int,
    val nameEn: String,
    val nameFr: String,
    val observedDate: String
):Parcelable