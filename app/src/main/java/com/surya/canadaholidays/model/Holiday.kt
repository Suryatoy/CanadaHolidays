package com.surya.canadaholidays.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Holiday(
    val date: String,
    val federal: Int,
    val id: Int,
    val nameEn: String,
    val nameFr: String,
    val observedDate: String
):Parcelable