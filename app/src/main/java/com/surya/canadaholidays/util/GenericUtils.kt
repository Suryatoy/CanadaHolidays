package com.surya.canadaholidays.util

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * To get current year
 */
fun getYear(): Int? {
    return Calendar.getInstance().get(Calendar.YEAR)
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
