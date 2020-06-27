package com.surya.canadaholidays.model

data class Holiday(
    val date: String,
    val federal: Int,
    val id: Int,
    val nameEn: String,
    val nameFr: String,
    val observedDate: String
)