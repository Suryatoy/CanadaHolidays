package com.surya.canadaholidays.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.snackbar.Snackbar
import com.surya.canadaholidays.BuildConfig
import com.surya.canadaholidays.R
import com.surya.canadaholidays.application.CanadaHolidaysApplication
import java.text.SimpleDateFormat
import java.util.*


private const val PREFERENCE_NAME = "com.surya.canadaholidays.preferences"

fun getSharedPreferences(context: Context): SharedPreferences? {
    return context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE)
}

/**
 * To add a string shared preference
 */
fun addPreference(name: String?, value: String?) {
    val editor = getSharedPreferences(CanadaHolidaysApplication.appContext)?.edit()
    editor?.putString(name, value)
    editor?.apply()
}

/**
 * To get a String shared preference value
 */
fun getStringPreference(name: String?, defaultValue: String?): String? {
    return getSharedPreferences(CanadaHolidaysApplication.appContext)?.getString(name, defaultValue)
}

/**
 * To add a Boolean shared preference
 */
fun addBooleanPreference(name: String?, value: Boolean) {
    val editor = getSharedPreferences(CanadaHolidaysApplication.appContext)?.edit()
    editor?.putBoolean(name, value)
    editor?.apply()
}

/**
 * To get a Boolean shared preference value
 */
fun getBooleanPreference(name: String?, defaultValue: Boolean): Boolean? {
    return getSharedPreferences(CanadaHolidaysApplication.appContext)?.getBoolean(
        name,
        defaultValue
    )
}

/**
 * To get current year
 */
fun getYear(): Int? {
    return Calendar.getInstance().get(Calendar.YEAR)
}

/**
 * To get current year
 */
fun getNextYear(): Int? {
    return Calendar.getInstance().get(Calendar.YEAR) + 1
}

/**
 * To Get the year from shared preferences
 */
fun getYearPref(): Int? {
    val yearPref = getStringPreference(Constants.YEAR_PREFERENCE, "")
    return if (yearPref.isNullOrEmpty() || yearPref.isNullOrBlank()) {
        getYear()
    } else {
        Integer.parseInt(yearPref)
    }
}

/**
 * To convert date string to easy readable format
 */
fun convertTimestamp(updatedDate: String?): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd MMM yyyy")
    return formatter.format(parser.parse(updatedDate))
}

/**
 * To show error in network calls snackbar
 */
fun showError(view: View, context: Context, errorMsg: String) {
    val snackBar = Snackbar.make(
        view, errorMsg,
        Snackbar.LENGTH_LONG
    ).setAction(null, null)
    snackBar.setActionTextColor(Color.WHITE)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(Color.BLACK)
    snackBar.show()
}

/**
 * To check the device is connected to Internet
 */
@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
    }
    return result
}

/**
 * To show information about views during app launch
 */
fun showInfoView(
    activity: Activity,
    view: View,
    title: String,
    description: String
) {
    TapTargetView.showFor(activity,  // `this` is an Activity
        TapTarget.forView(
            view,
            title,
            description
        ) // All options below are optional
            // .outerCircleColor(R.color.red) // Specify a color for the outer circle
            .outerCircleAlpha(0.96f) // Specify the alpha amount for the outer circle
            .targetCircleColor(R.color.white) // Specify a color for the target circle
            .titleTextSize(20) // Specify the size (in sp) of the title text
            .titleTextColor(R.color.white) // Specify the color of the title text
            .descriptionTextSize(15) // Specify the size (in sp) of the description text
            //   .descriptionTextColor(R.color.red) // Specify the color of the description text
            //   .textColor(R.color.blue) // Specify a color for both the title and description text
            .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
            //   .dimColor(R.color.black) // If set, will dim behind the view with 30% opacity of the given color
            .drawShadow(true) // Whether to draw a drop shadow or not
            .cancelable(true) // Whether tapping outside the outer circle dismisses the view
            .tintTarget(true) // Whether to tint the target view's color
            .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
            //.icon(R.drawable.) // Specify a custom drawable to draw as the target
            .targetRadius(100),  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                view.dismiss(true)
            }
        })



}
