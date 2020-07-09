package com.surya.canadaholidays.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.surya.canadaholidays.R
import com.surya.canadaholidays.view.fragment.HolidayListFragmentArgs


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val TAG = MainActivity::getLocalClassName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    /**
     * Initialising navigation components
     */
    private fun setUpNavigation() {
        navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController)
        navController.addOnDestinationChangedListener(mainDestinationChangedListener)
    }

    /**
     * To enable back navigation up arrow in fragments
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private val mainDestinationChangedListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        // compare destination id
        title = when (destination.id) {
            R.id.provincesListFragment -> getString(R.string.Provinces)
            R.id.holidayListFragment ->
                arguments?.let {
                    HolidayListFragmentArgs.fromBundle(it).province?.nameEn
                }
            else -> ""
        }
    }
}